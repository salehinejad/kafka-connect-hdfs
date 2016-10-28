package dp.hdfs.pathparse;

import org.junit.Test;

import java.sql.Timestamp;

import dp.hdfs.HdfsConfig;

/**
 * Created by mx on 10/28/16.
 */
public class PathFilterText {

    @Test
    public void testPath() {

        String filename = "mysql_host_172_17_0_1_port_3307.inventory.a+0+0000000000+0000000010.tsv";


        filename = filename.substring(0, filename.lastIndexOf(HdfsConfig.FILE_EXTENSION));

        System.out.println(filename);

        String[] parts = filename.split("[+]");
        System.out.println(parts[0]);
        System.out.println(parts[1]);
        System.out.println(parts[2]);
        System.out.println(parts[3]);

        String name = "mysql_host_172_17_0_1_port_3307.inventory.a+0+0000000000+0000000000";



    }

}
