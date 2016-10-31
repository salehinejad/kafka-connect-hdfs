package dp.hdfs.lines;


import io.confluent.connect.avro.AvroData;
import io.confluent.connect.hdfs.Format;
import io.confluent.connect.hdfs.RecordWriterProvider;
import io.confluent.connect.hdfs.SchemaFileReader;

public class TsvCsvFormat implements Format {
    public RecordWriterProvider getRecordWriterProvider() {
        return new LineWriterProvider();
    }

    public SchemaFileReader getSchemaFileReader(AvroData avroData) {
        return new LineFileReader(avroData);
    }
}
