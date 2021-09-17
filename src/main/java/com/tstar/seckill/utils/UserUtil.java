package com.tstar.seckill.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tstar.seckill.pojo.User;
import com.tstar.seckill.vo.RespBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author Jstar
 * @version 1.0
 * @date 2021/7/31 1:01 下午
 */
public class UserUtil {

    static String PASSWORD = "123456";
    static String username = "root";
    static String password = "1254";
    static String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    static String driver = "com.mysql.cj.jdbc.Driver";

    public static void createUser(int count) throws IOException, SQLException, ClassNotFoundException {

        List<User> users = new ArrayList<>(count);

        // 生成用户信息
        generateMiaoshaUserList(count, users);

        // 将用户信息插入数据库，以便在后面模拟用户登录时可以找到该用户，从而可以生成token返会给客户端，然后保存到文件中用于压测
        // 首次生成数据库信息的时候需要调用这个方法，非首次需要注释掉
//         insertSeckillUserToDB(users);


        // 模拟用户登录，生成token
        System.out.println("start to login...");
        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("/Users/jiahao/Desktop/config.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
        file.createNewFile();
        accessFile.seek(0);

        for (int i = 0; i < users.size(); i++) {
            // 模拟用户登录
            User user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream out = httpURLConnection.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFromPass(PASSWORD);
            out.write(params.getBytes());
            out.flush();

            // 生成token
            InputStream inputStream = httpURLConnection.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            System.out.println("response" +  response);
            ObjectMapper objectMapper = new ObjectMapper();
            RespBean respBean = objectMapper.readValue(response, RespBean.class);
            String ticket = (String) respBean.getObj();
            String row = user.getId() + "," + ticket;
            accessFile.seek(accessFile.length());
            accessFile.write(row.getBytes());
            accessFile.write("\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }
        accessFile.close();
        System.out.println("write token to file done!");
    }

    /**
     * 生成用户信息
     *  @param count 生成的用户数量
     * @param users 用于存储用户的list
     */
    private static void generateMiaoshaUserList(int count, List<User> users) {
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(19800000000L + i);// 模拟11位的手机号码
            user.setLoginCount(1);
            user.setNickname("user" + i);
            user.setRegisterData(new Date());
            user.setSalt("1a2b3c4d");
            user.setPassword(MD5Util.inputPassToDBPass(PASSWORD, user.getSalt()));
            users.add(user);
        }
    }

    /**
     * 将用户信息插入数据库中
     *
     * @param users 待插入的用户信息
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static void insertSeckillUserToDB(List<User> users) throws ClassNotFoundException, SQLException {
        System.out.println("start create user...");
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, username, password);
        String sql = "INSERT INTO t_user(login_count, nickname, register_data, salt, password, id)VALUES(?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            pstmt.setInt(1, user.getLoginCount());
            pstmt.setString(2, user.getNickname());
            pstmt.setTimestamp(3, new Timestamp(user.getRegisterData().getTime()));
            pstmt.setString(4, user.getSalt());
            pstmt.setString(5, user.getPassword());
            pstmt.setLong(6, user.getId());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.close();
        conn.close();
        System.out.println("insert to db done!");
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        createUser(5000);
    }

}
