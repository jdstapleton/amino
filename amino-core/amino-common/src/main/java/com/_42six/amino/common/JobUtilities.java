package com._42six.amino.common;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsShell;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class JobUtilities
{
    public static void resetWorkingDirectory(Configuration conf, String workingDir) throws Exception
    {
        deleteDirectory(conf, workingDir);
        final FsShell shell = new FsShell(conf);
        String[] command = new String[3];
        command[0] = "-mkdir";
        command[1] = "-p";
        command[2] = workingDir + "/failures";
        shell.run(command);

        setGroupAndPermissions(conf, workingDir);
    }

    public static void setGroupAndPermissions(Configuration conf, String workingDir) throws Exception
    {
        final FsShell shell = new FsShell(conf);
        final String workingGroup = conf.get("amino.hdfs.workingDirectory.group","accumulo");

        String[] command = new String[4];
        command[0] = "-chgrp";
        command[1] = "-R";
        command[2] = workingGroup;
        command[3] = workingDir;
        shell.run(command);

        command = new String[4];
        command[0] = "-chmod";
        command[1] = "-R";
        command[2] = "ugo=rwx"; // TODO - This probably isn't the most secure
        command[3] = workingDir;
        shell.run(command);

        shell.close();
    }

    public static void deleteDirectory(Configuration conf, String outputPath) throws Exception
    {
        final FsShell shell = new FsShell();
        shell.setConf(conf);

        final String[] delCommand = new String[3];
        delCommand[0] = "-rm";
        delCommand[1] = "-R";
        delCommand[2] = outputPath;
        shell.run(delCommand);
    }

    public static int failureDirHasFiles(Configuration conf, String failureDir) throws IOException
    {
        FileSystem fs = null;
        try {
            fs = FileSystem.get(conf);
            final FileStatus[] status = fs.listStatus(new Path(failureDir));
            int fileCount = status.length;
            System.out.println(fileCount + " files found in the failures directory on bulk import.");
            return (fileCount > 0) ? 1 : 0;
        } finally {
            if(fs != null){fs.close();}
        }
    }
}
