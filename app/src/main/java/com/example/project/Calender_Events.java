package com.example.project;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;

public class Calender_Events extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextTitle, editTextDescription;
    private TextView textViewDate, textViewTime;
    private Button buttonDate, buttonTime, buttonSave;

    DateFormat timeFormat;
    DateFormat dateFormat;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_events);;

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        textViewDate = findViewById(R.id.text_view_date);
        textViewTime = findViewById(R.id.text_view_time);

        buttonDate = findViewById(R.id.button_date);
        buttonTime = findViewById(R.id.button_time);
        buttonSave = findViewById(R.id.button_save);

        buttonDate.setOnClickListener(this);
        buttonTime.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

        calendar = Calendar.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_date:
                showDatePickerDialog();
                break;
            case R.id.button_time:
                showTimePickerDialog();
                break;
            case R.id.button_save:
                saveEvent();
                createNotificationChannel();

                break;
        }
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateTimeViews();
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        updateDateTimeViews();
                    }
                }, hourOfDay, minute, true);
        timePickerDialog.show();
    }

    private void updateDateTimeViews() {
         dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
         timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

        textViewDate.setText(dateFormat.format(calendar.getTime()));
        textViewTime.setText(timeFormat.format(calendar.getTime()));
    }

    private void saveEvent() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String date = textViewDate.getText().toString().trim();
        String time = textViewTime.getText().toString().trim();

        if (title.isEmpty()) {
            editTextTitle.setError("Title is required");
            editTextTitle.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            editTextDescription.setError("Description is required");
            editTextDescription.requestFocus();
            return;
        }

        if (date.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();

            if (time.isEmpty()) {
                Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: save the event to database or perform any other desired action
            Toast.makeText(this, "Event saved successfully", Toast.LENGTH_SHORT).show();
        }
        DateFormat dateTimeFormat = DateFormat.getDateTimeInstance();
//        String dateTimeString = date + " " + time;
        String dateTimeString = dateTimeFormat.format(calendar.getTime());
        ParsePosition pos = new ParsePosition(0);
        Date dateTime = dateTimeFormat.parse(dateTimeString,pos);
        Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateTime);
        sendNotification(calendar, title, description);
//        try {
//            Date dateTime = dateTimeFormat.parse(dateTimeString);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(dateTime);
//            sendNotification(calendar, title, description);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error parsing date/time", Toast.LENGTH_SHORT).show();
//        }

    }
    public void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("event_notification_channel",
                    "Event Notification Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("This channel is for event notifications");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(Calendar calendar, String title, String message) {
        Intent intent = new Intent(this, Calender_Events.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "event_notification_channel")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//        long triggerTime = calendar.getTimeInMillis();
//        notificationManager.notify((int) triggerTime, builder.build());

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        notificationIntent.putExtra("notification", builder.build());
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);


        Toast.makeText(this, "Notification set for " + DateFormat.getDateTimeInstance().format(calendar.getTime()), Toast.LENGTH_SHORT).show();
    }

}