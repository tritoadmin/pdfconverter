package kr.co.trito.pdf.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileCommonUtil {

    private static final int BUFFER_SIZE = 8192;

    /* ==============================
     * 파일명 안전 처리
     * ============================== */

    /** 파일명 특수문자 제거 */
    public static String sanitizeFileName(String fileName) {
        if (fileName == null) return "";
        return fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

    /** UUID 파일명 생성 */
    public static String generateUniqueFileName(String originalName) {
        String ext = getExtension(originalName);
        return UUID.randomUUID().toString().replace("-", "")
                + (ext.length() > 0 ? "." + ext : "");
    }

    /* ==============================
     * 파일 저장
     * ============================== */

    /** InputStream → 파일 저장 */
    public static File saveFile(InputStream in, String saveDir, String fileName)
            throws IOException {

        mkdirs(saveDir);

        File file = new File(saveDir, sanitizeFileName(fileName));
        OutputStream out = null;

        try {
            out = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } finally {
            closeQuietly(out);
            closeQuietly(in);
        }
        return file;
    }

    /* ==============================
     * 파일 다운로드
     * ============================== */

    /** 공통 파일 다운로드 */
    public static void download(HttpServletRequest request,
                                HttpServletResponse response,
                                File file,
                                String downloadName) throws IOException {

        if (file == null || !file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String userAgent = request.getHeader("User-Agent");
        String encodedName;

        if (userAgent != null && userAgent.indexOf("MSIE") > -1) {
            encodedName = URLEncoder.encode(downloadName, "UTF-8").replaceAll("\\+", "%20");
        } else {
            encodedName = new String(downloadName.getBytes("UTF-8"), "ISO-8859-1");
        }

        response.setContentType("application/octet-stream");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + encodedName + "\"");

        InputStream in = null;
        OutputStream out = null;

        try {
            in = new BufferedInputStream(new FileInputStream(file));
            out = response.getOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } finally {
            closeQuietly(in);
        }
    }

    /* ==============================
     * 파일 검증
     * ============================== */

    /** 확장자 허용 여부 */
    public static boolean isAllowedExtension(String fileName, String[] allowExt) {
        String ext = getExtension(fileName).toLowerCase();
        for (int i = 0; i < allowExt.length; i++) {
            if (ext.equals(allowExt[i].toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /** 파일 사이즈 제한 (MB) */
    public static boolean checkFileSize(File file, int maxMb) {
        long maxSize = maxMb * 1024L * 1024L;
        return file.length() <= maxSize;
    }

    /* ==============================
     * 경로 / 파일 정보
     * ============================== */

    public static String getExtension(String fileName) {
        if (fileName == null) return "";
        int idx = fileName.lastIndexOf('.');
        return (idx > -1) ? fileName.substring(idx + 1) : "";
    }

    public static boolean mkdirs(String path) {
        File dir = new File(path);
        return dir.exists() || dir.mkdirs();
    }

    /* ==============================
     * 리소스 종료
     * ============================== */

    public static void closeQuietly(Closeable c) {
        try {
            if (c != null) c.close();
        } catch (IOException ignore) {
        }
    }


    /** 파일 존재 여부 */
    public static boolean exists(String path) {
        if (path == null) return false;
        return new File(path).exists();
    }

    /** 파일 삭제 */
    public static boolean delete(String path) {
        if (path == null) return false;
        File file = new File(path);
        return file.exists() && file.delete();
    }

    /** 디렉토리 하위 파일 전체 삭제 */
    public static void deleteDirectory(File dir) {
        if (dir == null || !dir.exists()) return;

        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteDirectory(f);
                } else {
                    f.delete();
                }
            }
        }
        dir.delete();
    }

    /** 파일 복사 */
    public static void copyFile(File src, File dest) throws IOException {
        if (src == null || dest == null) return;

        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dest);

            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } finally {
            closeQuietly(in);
            closeQuietly(out);
        }
    }

    /** 파일 이동 */
    public static boolean moveFile(File src, File dest) throws IOException {
        copyFile(src, dest);
        return src.delete();
    }

    /** 파일 내용 문자열로 읽기 */
    public static String readFileToString(File file, String charset) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), charset));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } finally {
            closeQuietly(reader);
        }
        return sb.toString();
    }

    /** 문자열 파일로 쓰기 */
    public static void writeStringToFile(File file, String data, String charset)
            throws IOException {

        Writer writer = null;
        try {
            writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file), charset));
            writer.write(data);
        } finally {
            closeQuietly(writer);
        }
    }

    /** 파일명만 추출 */
    public static String getFileName(String path) {
        if (path == null) return "";
        return new File(path).getName();
    }

    /** 파일 사이즈(byte) */
    public static long getFileSize(File file) {
        return (file != null && file.exists()) ? file.length() : 0L;
    }

    /** 특정 확장자 파일 목록 조회 */
    public static List<File> listFilesByExtension(File dir, final String ext) {
        List<File> result = new ArrayList<File>();
        if (dir == null || !dir.isDirectory()) return result;

        File[] files = dir.listFiles();
        if (files == null) return result;

        for (File f : files) {
            if (f.isFile() && f.getName().toLowerCase().endsWith("." + ext)) {
                result.add(f);
            }
        }
        return result;
    }


}