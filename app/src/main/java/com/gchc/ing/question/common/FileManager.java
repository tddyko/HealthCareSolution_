package com.gchc.ing.question.common;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Created by jihoon on 2016-03-21.
 * 파일관리자 클래스
 * @since 0, 1
 */
public class FileManager {
    //	 실제저장위치 : sdcard/0/Android/data/com.sec.chatonlive/files/images
    private Context mContext;
    private	String				FILE_PATH;
    private File mFile;


    /**
     * 파일관리자 생성자
     * @param context   context
     * @param type  타입 ( 0 - 이미지 , 1 - 음악파일 )
     */
    public FileManager(Context context, int type)
    {
        this.mContext = context;
        CLog.i("FileManager()");
        try {
            File file = context.getExternalFilesDir(null);
//            File file = context.getExternalCacheDir();
            switch( type ){
                case 0:
                    FILE_PATH = file.getPath() + "/image/";
                    break;
                case 1:
                    FILE_PATH = file.getPath() + "/voice/";
                    break;
            }
            CLog.i("FILE_PATH = " +FILE_PATH);
            //FILE_PATH = mContext.getExternalFilesDir(null).getPath() + "/image/";
        }
        catch(Exception e) {
            CLog.e(e.toString());
        }


        if ( FILE_PATH == null ) {
            switch( type ){
                case 0:
                    FILE_PATH = mContext.getCacheDir().getPath() + "/image/";
                    break;
                case 1:
                    FILE_PATH = mContext.getCacheDir().getPath() + "/voice/";
                    break;
            }
        }

        CLog.i("FILE FILE_PATH : " + FILE_PATH);
        mFile = new File(FILE_PATH);
        if (!mFile.exists())
        {
            CLog.i("Create Dir OK");
            mFile.mkdirs();
        }
    }

    /**
     * 파일경로 가져오기
     * @return file_path
     */
    public String getFilePath()
    {
        return FILE_PATH;

    }

    public File getPostingImageDir()
    {
        return mFile;
    }


    /**
     * 디렉토리 생성
     * @return dir
     */
    public File makeDirectory(String dir_path)
    {
        File dir = new File(dir_path);
        if (!dir.exists())
        {
            dir.mkdirs();
            CLog.i("!dir.exists" );
        }else{
            CLog.i("dir.exists" );
        }
        return dir;
    }
    public File makeImageDirectory()
    {
        if (!mFile.exists())
        {
            mFile.mkdirs();
//			CLog.i( TAG , "!dir.exists" );
        }else{
//			CLog.i( TAG , "dir.exists" );
        }
        return mFile;
    }

    /**
     * 파일 생성
     * @param dir
     * @return file
     */
    public File makeFile(File dir , String file_path)
    {
        File file = null;
        boolean isSuccess = false;
        if(dir.isDirectory())
        {
            file = new File(file_path);
            if(file!=null&&!file.exists())
            {
                CLog.i("!file.exists");
                try {
                    isSuccess = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally{
                    CLog.i("file create = " + isSuccess);
                }
            }else{
                CLog.i(  "file.exists" );
            }
        }
        return file;
    }

    /**
     * (dir/file) 절대 경로 얻어오기
     * @param file
     * @return String
     */
    public String getAbsolutePath(File file)
    {
        return ""+file.getAbsolutePath();
    }

    /**
     * (dir/file) 삭제 하기
     * @param file
     */
    public boolean deleteFile(File file)
    {
        boolean result;
        if(file!=null&&file.exists())
        {
            file.delete();
            result = true;
        }else{
            result = false;
        }
        return result;
    }

    /**
     * 파일여부 체크 하기
     * @param file
     * @return
     */
    public boolean isFile(File file)
    {
        boolean result;
        if(file!=null&&file.exists()&&file.isFile())
        {
            result=true;
        }else{
            result=false;
        }
        return result;
    }

    /**
     * 디렉토리 여부 체크 하기
     * @param dir
     * @return
     */
    public boolean isDirectory(File dir)
    {
        boolean result;
        if(dir!=null&&dir.isDirectory())
        {
            result=true;
        }else{
            result=false;
        }
        return result;
    }

    public boolean isDirectorys()
    {
        boolean result;
        if(mFile!=null&&mFile.isDirectory())
        {
            result=true;
        }else{
            result=false;
        }
        return result;
    }

    /**
     * 파일 존재 여부 확인 하기
     * @param file
     * @return
     */
    public boolean isFileExist(File file)
    {
        boolean result;
        if(file!=null&&file.exists())
        {
            result=true;
        }else{
            result=false;
        }
        return result;
    }

    /**
     * 파일 이름 바꾸기
     * @param file
     */
    public boolean reNameFile(File file , File new_name)
    {
        boolean result;
        if(file!=null&&file.exists()&&file.renameTo(new_name))
        {
            result=true;
        }else{
            result=false;
        }
        return result;
    }

    /**
     * 디렉토리에 안에 내용을 보여 준다.
     * @param dir 파일
     * @return
     */
    public String[] getList(File dir)
    {
        if(dir!=null&&dir.exists())
            return dir.list();
        return null;
    }

    /**
     * 파일에 내용 쓰기
     * @param file
     * @param file_content
     * @return
     */
    public boolean writeFile(File file , byte[] file_content)
    {
        boolean result;
        FileOutputStream fos;
        if(file!=null&&file.exists()&&file_content!=null)
        {
            try{
                fos = new FileOutputStream(file);
                try {
                    fos.write(file_content);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            result = true;
        }else{
            result = false;
        }
        return result;
    }

    /**
     * 파일 읽어 오기
     * @param file
     */
    public void readFile(File file)
    {
        int readcount=0;
        if(file!=null&&file.exists())
        {
            try {
                FileInputStream fis = new FileInputStream(file);
                readcount = (int)file.length();
                byte[] buffer = new byte[readcount];
                fis.read(buffer);
                for(int i=0 ; i<file.length();i++){
                    CLog.d(""+buffer[i]);
                }
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath)
    {

        File fileCacheItem = new File(FILE_PATH+strFilePath);
        OutputStream out = null;

        //CLog.e("ERROR", "파일 : " + FILE_PATH+strFilePath);

        try	{
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        }
        catch (Exception e)	{
            e.printStackTrace();
        }
        finally	{

            try	{
                out.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath, int quality)
    {

        File fileCacheItem = new File(FILE_PATH+strFilePath);
        OutputStream out = null;

        //CLog.e("ERROR", "파일 : " + FILE_PATH+strFilePath);

        try	{
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);

            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);

        }
        catch (Exception e)	{
            e.printStackTrace();
        }
        finally	{

            try	{
                out.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
