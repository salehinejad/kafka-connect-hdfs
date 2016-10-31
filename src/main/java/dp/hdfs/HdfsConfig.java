package dp.hdfs;

/**
 * Created by mx on 10/28/16.
 */
public class HdfsConfig {

    //TODO  change by sinktask, this is default
    public static String HDFS_LINE_SPLIT = ",";

    public static String FILE_EXTENSION = ".csv";

    //filename like mysql_host_172_17_0_1_port_3307.inventory.a+0+0000000000+0000000000.tsv
    public static final String DP_COMMMITTED_FILENAME_SEPARATOR_REGEX = "[+]";


}
