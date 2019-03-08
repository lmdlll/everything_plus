package com.lmd.config;


//引用单例

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;



@Getter
public class everythingConfig {
    private static volatile everythingConfig config;
    private everythingConfig(){}

    //建立索引文件的路径
    private Set<String> includePath = new HashSet<>();
    //排除索引文件的路径
    private Set<String> excludePath = new HashSet<>();

    //TODO 可配置的参数可能会在此体现
    //检索最大的返回值数量,默认值30个
    @Setter
    private Integer maxReturn = 30;

    //深度排序的规则，默认是升序
    @Setter
    private Boolean depthOrderAsc = true;

    /**
     * H2数据库文件路径
     */
    private String h2IndexPath = System.getProperty("user.dir")+File.separator+"everything_plus";



    //初始化默认的配置
    private void initDefaultPathsConfig(){
        //1.获取文件系统
        FileSystem fileSystem = FileSystems.getDefault();
        //遍历的目录
        Iterable<Path> iterable = fileSystem.getRootDirectories();
        iterable.forEach(path -> config.includePath.add(path.toString()));
        //排除的目录
        //windows : C:\Windows   C:\Program Files (x86)  C:\Program Files   C:\ProgramData
        //Linux: /tmp   /etc
        String osname = System.getProperty("os.name");
        if(osname.startsWith("Windows")){
            config.getExcludePath().add("C:\\Windows");
            config.getExcludePath().add("C:\\Program Files (x86)");
            config.getExcludePath().add("C:\\Program Files");
            config.getExcludePath().add("C:\\ProgramData");
        }else {
            config.getExcludePath().add("/tmp");
            config.getExcludePath().add("/etc");
            config.getExcludePath().add("/root");
        }
    }


    public static everythingConfig getInstance(){
        if (config==null){
            synchronized (everythingConfig.class){
                if(config==null){
                    config = new everythingConfig();
                    config.initDefaultPathsConfig();
                }
            }
        }
        return config;
    }

//    public static void main(String[] args) {
//        everythingConfig config = everythingConfig.getInstance();
//        System.out.println(config.getIncludePath());
//        System.out.println(config.getExcludePath());
//
//    }

}
