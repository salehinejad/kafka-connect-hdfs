/**
 * Copyright 2015 Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 **/

package io.confluent.connect.hdfs;

import org.apache.hadoop.fs.Hdfs;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import dp.hdfs.HdfsConfig;
import io.confluent.connect.avro.AvroData;
import io.confluent.connect.hdfs.schema.Compatibility;
import io.confluent.connect.hdfs.schema.SchemaUtils;

public class HdfsSinkTask extends SinkTask {

    private static final Logger log = LoggerFactory.getLogger(HdfsSinkTask.class);
    private DataWriter hdfsWriter;
    private AvroData avroData;

    public HdfsSinkTask() {

    }

    @Override
    public String version() {
        return Version.getVersion();
    }

    @Override
    public void start(Map<String, String> props) {
        Set<TopicPartition> assignment = context.assignment();
        ;
        try {
            HdfsSinkConnectorConfig connectorConfig = new HdfsSinkConnectorConfig(props);
            int schemaCacheSize = connectorConfig.getInt(HdfsSinkConnectorConfig.SCHEMA_CACHE_SIZE_CONFIG);
            avroData = new AvroData(schemaCacheSize);
            hdfsWriter = new DataWriter(connectorConfig, context, avroData);

            String linesplit;
            if ((linesplit = connectorConfig.getString("line.split")) != null) {
                if (linesplit.contains("tsv")) {
                    HdfsConfig.FILE_EXTENSION = ".tsv";
                    HdfsConfig.HDFS_LINE_SPLIT = "\t";
                } else {
                    HdfsConfig.FILE_EXTENSION = ".csv";
                    HdfsConfig.HDFS_LINE_SPLIT = ",";
                }
            }
            recover(assignment);
        } catch (ConfigException e) {
            throw new ConnectException("Couldn't start HdfsSinkConnector due to configuration error.", e);
        } catch (ConnectException e) {
            log.info("Couldn't start HdfsSinkConnector:", e);
            log.info("Shutting down HdfsSinkConnector.");
            if (hdfsWriter != null) {
                hdfsWriter.close(assignment);
                hdfsWriter.stop();
            }
        }
    }

    @Override
    public void stop() throws ConnectException {
        if (hdfsWriter != null) {
            hdfsWriter.stop();
        }
    }

    @Override
    public void put(Collection<SinkRecord> records) throws ConnectException {
        try {
            hdfsWriter.write(records);
        } catch (ConnectException e) {
            throw new ConnectException(e);
        }
    }

    @Override
    public void flush(Map<TopicPartition, OffsetAndMetadata> offsets) {
        // Do nothing as the connector manages the offset
    }

    @Override
    public void open(Collection<TopicPartition> partitions) {
        hdfsWriter.open(partitions);
    }

    @Override
    public void close(Collection<TopicPartition> partitions) {
        hdfsWriter.close(partitions);
    }

    private void recover(Set<TopicPartition> assignment) {
        for (TopicPartition tp : assignment) {
            hdfsWriter.recover(tp);
        }
    }

    public AvroData getAvroData() {
        return avroData;
    }
}
