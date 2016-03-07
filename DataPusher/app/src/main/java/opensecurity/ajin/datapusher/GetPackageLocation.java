package opensecurity.ajin.datapusher;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;


public class GetPackageLocation extends Service {
    public GetPackageLocation() {
    }
    private ProgressDialog dialog = null;
    private Context context = null;
    private boolean suAvailable = false;
    private String suVersion = null;
    private String suVersionInternal = null;
    private List<String> suResult = null;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String file = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mobsec_status";
        File f = new File(file);
        if (f.exists())
        {
             f.delete();
        }


        String pkg = "";
            pkg = intent.getAction().toString();
            Log.d("MobSecFramework", "Received Package Name: " + pkg);
         suAvailable = Shell.SU.available();
        if (suAvailable) {
            suVersion = Shell.SU.version(false);
            suVersionInternal = Shell.SU.version(true);
            suResult = Shell.SU.run(new String[] {
                    "tar -cvf /data/local/"+pkg+".tar /data/data/"+pkg+"/",
            });
            suResult = Shell.SU.run(new String[] {
                    "chmod 777 /data/local/"+pkg+".tar",
            });
            Log.d("MobSecFramework","tar Created on SDCARD.");
            String filename = "mobsec_status";
            File f1 = new File("/sdcard/mobsec_status");
            FileOutputStream fos;
            byte[] data = new String("MOBSEC-TAR-CREATED").getBytes();
            try {
                fos = new FileOutputStream(f1);
                fos.write(data);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                // handle exception
            } catch (IOException e) {
                // handle exception
            }
             }
        Log.d("MobSecFramework","Operation Completed!");
        return START_STICKY;
    //adb shell am startservice -a com.google.blaa opensecurity.ajin.datapusher/.GetPackageLocation
    }
    @Override
    public void onCreate()
    {
        super.onCreate();

    }

}
