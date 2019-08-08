package online.iizvv.signature;

import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.lang.UUID;
import online.iizvv.core.config.Config;
import online.iizvv.utils.Shell;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SignatureApplicationTests {


    @Test
    public void contextLoads() throws IOException, InterruptedException {

        creatUDIDMobileconfig(2);
    }

    void creatUDIDMobileconfig(long id) throws IOException, InterruptedException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n" +
                "<plist version=\"1.0\">\n" +
                "    <dict>\n" +
                "        <key>PayloadContent</key>\n" +
                "        <dict>\n" +
                "            <key>URL</key>\n" +
                "            <string>"+ Config.udidURL +"/udid/getUDID?id="+ id +"</string> <!--接收数据的接口地址-->\n" +
                "            <key>DeviceAttributes</key>\n" +
                "            <array>\n" +
                "                <string>SERIAL</string>\n" +
                "                <string>MAC_ADDRESS_EN0</string>\n" +
                "                <string>UDID</string>\n" +
                "                <string>IMEI</string>\n" +
                "                <string>ICCID</string>\n" +
                "                <string>VERSION</string>\n" +
                "                <string>PRODUCT</string>\n" +
                "            </array>\n" +
                "        </dict>\n" +
                "        <key>PayloadOrganization</key>\n" +
                "        <string>" + Config.payloadOrganization +"</string>  <!--组织名称-->\n" +
                "        <key>PayloadDisplayName</key>\n" +
                "        <string>" + Config.payloadDisplayName + "</string>  <!--安装时显示的标题-->\n" +
                "        <key>PayloadVersion</key>\n" +
                "        <integer>1</integer>\n" +
                "        <key>PayloadUUID</key>\n" +
                "        <string>"+ UUID.randomUUID().toString().replace("-", "") +"</string>  <!--自己随机填写的唯一字符串-->\n" +
                "        <key>PayloadIdentifier</key>\n" +
                "        <string>online.iizvv.profile-service</string>\n" +
                "        <key>PayloadDescription</key>\n" +
                "        <string>"+Config.payloadDescription+"</string>   <!--描述-->\n" +
                "        <key>PayloadType</key>\n" +
                "        <string>Profile Service</string>\n" +
                "    </dict>\n" +
                "</plist>";
        ClassLoader classLoader = this.getClass().getClassLoader();
        String name = "udid_"+id;
        String filePath = name + ".mobileconfig";
        FileWriter writer = new FileWriter(filePath);
        writer.write(xml);
        String serverKey = classLoader.getResource("server.key").getPath();
        String ca = classLoader.getResource("ca.crt").getPath();
        String serverCrt = classLoader.getResource("server.crt").getPath();
        String filePath2 = name+"_"+id +".mobileconfig";
        String com = "openssl smime -sign -in " + filePath +" -out "+ filePath2 + " -signer "+serverCrt+" -inkey "+serverKey+"  -certfile "+ca+" -outform der -nodetach";
        System.out.println(filePath2);
        Shell.run(com);
    }


}
