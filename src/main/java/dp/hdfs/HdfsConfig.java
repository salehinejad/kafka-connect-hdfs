package dp.hdfs;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by mx on 10/28/16.
 */
public class HdfsConfig {

    private static final String KEY_LINE_SPLIT = "LINE_SPLIT";
    public static final String HDFS_LINE_SPLIT = StringUtils.isBlank(System.getenv(KEY_LINE_SPLIT)) ? "," : System.getenv(KEY_LINE_SPLIT);

    private static final String KEY_FILE_EXTENSION = "FILE_EXTENSION";
    public static final String FILE_EXTENSION = StringUtils.isBlank(System.getenv(KEY_FILE_EXTENSION)) ? ".tsv" : System.getenv(KEY_FILE_EXTENSION);


    //filename like mysql_host_172_17_0_1_port_3307.inventory.a+0+0000000000+0000000000.tsv
    public static final String DP_COMMMITTED_FILENAME_SEPARATOR_REGEX = "[+]";


}
