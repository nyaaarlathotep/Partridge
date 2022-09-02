package cn.nyaaar.partridgemngservice.util;


import java.io.*;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {

    private static final String tmpDir =  System.getProperty("java.io.tmpdir");

	/**
	 * 文件byte[]类型转File
	 * @param fileBytes
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static File byte2File(byte[] fileBytes, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if(!dir.exists()) { //判断文件目录是否存在
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(fileBytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	/**
	 * 文件File类型转byte[]
	 * @param filePath
	 * @return
	 */
	public static byte[] file2Byte(String filePath) {
		byte[] fileBytes = null;
		FileInputStream fis = null;
		try {
			File file = new File(filePath);
			fis = new FileInputStream(file);
			fileBytes = new byte[(int) file.length()];
			fis.read(fileBytes);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileBytes;
	}

	/**
	 * 文件File类型转byte[]
	 * @param file
	 * @return
	 */
	public static byte[] file2Byte(File file) {
		byte[] fileBytes = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			fileBytes = new byte[(int) file.length()];
			fis.read(fileBytes);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileBytes;
	}

	/**
	 * 文件File类型转BASE64
	 * @param file
	 * @return
	 */
	public static String file2Base64(File file) {
		return Base64.getEncoder().encodeToString(file2Byte(file));
	}

    /**
     * 文件byte[]转BASE64
     *
     * @param bytes bytes
     * @return
     */
    public static String bytes2Base64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static File base642File(String base64, String fileName) {
        return byte2File(Base64.getDecoder().decode(base64), tmpDir, fileName);
    }

	/**
	 * 计算文件hash值
	 */
    public static String hashFile(File file) throws Exception {
	    FileInputStream fis = null;
	    String sha256 = null;
	    try {
		    fis = new FileInputStream(file);
		    MessageDigest md = MessageDigest.getInstance("SHA-256");
		    byte buffer[] = new byte[1024];
		    int length = -1;
		    while ((length = fis.read(buffer, 0, 1024)) != -1) {
			    md.update(buffer, 0, length);
		    }
		    byte[] digest = md.digest();
		    sha256 = byte2hexLower(digest);
	    } catch (Exception e) {
		    e.printStackTrace();
		    throw new Exception("计算文件hash值错误");
	    } finally {
		    try {
			    if(fis != null) {
				    fis.close();
			    }
		    } catch (IOException e) {
			    e.printStackTrace();
		    }
	    }
	    return sha256;
    }

    /**
     * 文件byte[]类型转File, 追加
     * @param fileBytes
     * @param filePath
     * @param fileName
     * @return
     */
    public static File byte2File(byte[] fileBytes, String filePath, String fileName, boolean isAppend) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()) { //判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file, isAppend);
            bos = new BufferedOutputStream(fos);
            bos.write(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    public static void file2Zip(String sourceFilePath, String zipFilePath, String fileName) throws Exception {
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        if (!sourceFile.exists()) {
            throw new Exception("待压缩的文件目录：" + sourceFilePath + " 不存在");
        } else {
            try {
                File zipFile = new File(zipFilePath + File.separator + fileName + ".zip");
                if (zipFile.exists()) {
                    throw new Exception(zipFilePath + " 目录下存在名字为：" + fileName + ".zip" + "文件");
                }
                File[] sourceFiles = sourceFile.listFiles();
                if (null == sourceFiles || sourceFiles.length < 1) {
                    throw new Exception("待压缩的文件目录：" + sourceFilePath + " 里面不存在文件,无需压缩.");
                }
                fos = new FileOutputStream(zipFile);
                zos = new ZipOutputStream(new BufferedOutputStream(fos));
                byte[] bufs = new byte[1024 * 10];
                for (int i = 0; i < sourceFiles.length; i++) {
                    // 创建ZIP实体,并添加进压缩包
                    ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                    zos.putNextEntry(zipEntry);
                    // 读取待压缩的文件并写进压缩包里
                    fis = new FileInputStream(sourceFiles[i]);
                    bis = new BufferedInputStream(fis, 1024 * 10);
                    int read = 0;
                    while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                        zos.write(bufs, 0, read);
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally { // 关闭流
                try {
                    if (null != bis) {
                        bis.close();
                    }
                    if (null != zos) {
                        zos.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

	private static String byte2hexLower(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int i = 0; i < b.length; i++) {
			stmp = Integer.toHexString(b[i] & 0XFF);
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs;
	}

}
