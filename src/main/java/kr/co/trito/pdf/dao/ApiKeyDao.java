package kr.co.trito.pdf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Repository;

import kr.co.trito.pdf.vo.ApiKeyInfo;

@Repository
public class ApiKeyDao {

    public ApiKeyInfo findByAccessKey(Connection conn, String accessKey)
            throws Exception {

        String sql =
            "SELECT ACCESS_KEY, SECRET_KEY, PERMISSION, STATUS, EXPIRE_DATE " +
            "FROM API_CLIENT_KEY WHERE ACCESS_KEY = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, accessKey);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            ApiKeyInfo info = new ApiKeyInfo();
            info.setAccessKey(rs.getString("ACCESS_KEY"));
            info.setSecretKey(rs.getString("SECRET_KEY"));
            info.setPermission(rs.getString("PERMISSION"));
            info.setStatus(rs.getString("STATUS"));
            info.setExpireDate(rs.getDate("EXPIRE_DATE"));
            return info;
        }
        return null;
    }


    public void insertLog(Connection conn, String accessKey, HttpServletRequest req, int resultCode, String message) throws Exception {

		String sql ="INSERT INTO API_REQUEST_LOG (ACCESS_KEY, REQUEST_URI, HTTP_METHOD, RESULT_CODE, CLIENT_IP, MESSAGE) " +
		                                         "VALUES (?, ?, ?, ?, ?, ?)";

		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, accessKey);
		ps.setString(2, req.getRequestURI());
		ps.setString(3, req.getMethod());
		ps.setInt(4, resultCode);
		ps.setString(5, req.getRemoteAddr());
		ps.setString(6, message);
		ps.executeUpdate();
	}


}
