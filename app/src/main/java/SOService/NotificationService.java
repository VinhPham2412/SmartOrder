package SOService;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.su21g3project.Customer.CBookedActivity;
import com.example.su21g3project.General.NoticeActivity;
import com.example.su21g3project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import Model.Notice;
import Model.Order;
import Model.User;

public class NotificationService extends Service {

    private static final String CHANNEL_ID = "SmartOrderChanel";
    private DatabaseReference reference;
    private FirebaseUser user;
    private NotificationManagerCompat notificationManager;
    private String role = "";

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startOwnForeground();
        } else
            startForeground(1, new Notification());
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.deleteNotificationChannel(CHANNEL_ID);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void startOwnForeground() {
        createChannel();
        notificationManager = NotificationManagerCompat.from(getApplicationContext());
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        /**
         * notify when message replied
         */
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    role = user.getRole();
                    reference = FirebaseDatabase.getInstance().getReference("Communications").child(role);
                    reference.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                if (snapshot1.exists()) {
                                    Notice notice = snapshot1.getValue(Notice.class);
                                    if (notice.getIsReply() && !notice.getIsNotify()) {
                                        notifiedMess(notice.getId(), notice.getMessageReply(), role);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //notify when order accepted or rejected
        reference = FirebaseDatabase.getInstance().getReference("Orders");
        reference.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.exists()) {
                        Order order = snapshot1.getValue(Order.class);
                        if ((order.getStatus().equals("accepted") ||
                                order.getStatus().equals("rejected")) &&
                                !order.getIsNotify()) {
                            notifiedOrder(order.getId(), order.getStatus());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        /**
         *this for display that service is running only
         */
        int notificationId = new Random().nextInt(99999);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(notificationId, notification);
    }

    private void notifiedOrder(String id, String status) {
        int notificationId = new Random().nextInt(99999);
        reference = FirebaseDatabase.getInstance().getReference("Orders").child(id).child("isNotify");
        reference.setValue(true);
        Intent intent = new Intent(getApplicationContext(), CBookedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.app_ic)
                .setContentTitle("Smart Order")
                .setContentText("Your order has been : " + status)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
        Log.println(Log.INFO, "show notify", "showing notify");
    }

    private void notifiedMess(String id, String messageReply, String path) {
        reference = FirebaseDatabase.getInstance().getReference("Communications").child(path).child(id).child("isNotify");
        reference.setValue(true);
        int notificationId = new Random().nextInt(99999);
        Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
        intent.putExtra("role",path);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.app_ic)
                .setContentTitle("Smart Order")
                .setContentText("Manager:\n" + messageReply)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }
}
