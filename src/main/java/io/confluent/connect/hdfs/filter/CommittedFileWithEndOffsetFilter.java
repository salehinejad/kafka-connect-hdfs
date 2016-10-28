package io.confluent.connect.hdfs.filter;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

import dp.hdfs.HdfsConfig;

public class CommittedFileWithEndOffsetFilter implements PathFilter {
    private CommittedFileFilter filter;
    private long endOffset;

    public CommittedFileWithEndOffsetFilter(CommittedFileFilter filter, long endOffset) {
        this.filter = filter;
        this.endOffset = endOffset;
    }

    @Override
    public boolean accept(Path path) {
        String filename = path.getName();
        filename = filename.substring(0, filename.lastIndexOf(HdfsConfig.FILE_EXTENSION));
        String[] parts = filename.split(HdfsConfig.DP_COMMMITTED_FILENAME_SEPARATOR_REGEX);
        return filter.accept(path) && Long.parseLong(parts[3]) == endOffset;
    }
}
