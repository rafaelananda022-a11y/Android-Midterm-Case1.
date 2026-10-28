
package com.example.studentmanagement;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.database.Cursor;

public class MainActivity extends Activity {

    EditText studentName, studentId;
    TextView ageLabel;
    SeekBar ageSlider;
    DatabaseHelper database;

    int age = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentName = findViewById(R.id.studentName);
        studentId = findViewById(R.id.studentId);
        ageLabel = findViewById(R.id.ageLabel);
        ageSlider = findViewById(R.id.ageSlider);

        Button add = findViewById(R.id.addButton);
        Button edit = findViewById(R.id.editButton);
        Button delete = findViewById(R.id.deleteButton);
        Button view = findViewById(R.id.viewButton);

        database = new DatabaseHelper(this);

        ageSlider.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar, int progress,
                            boolean fromUser) {
                        age = 17 + progress;
                        ageLabel.setText("Student Age: " + age);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                }
        );

        add.setOnClickListener(v -> {
            String id = studentId.getText().toString().trim();
            String name = studentName.getText().toString().trim();

            if (id.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Fill in all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            boolean result = database.addStudent(id, name, age);

            Toast.makeText(this,
                    result ? "Student added" : "Failed: ID may exist",
                    Toast.LENGTH_SHORT).show();
        });

        edit.setOnClickListener(v -> {
            boolean result = database.editStudent(
                    studentId.getText().toString().trim(),
                    studentName.getText().toString().trim(), age);

            Toast.makeText(this,
                    result ? "Student updated" : "Student not found",
                    Toast.LENGTH_SHORT).show();
        });

        delete.setOnClickListener(v -> {
            boolean result = database.deleteStudent(
                    studentId.getText().toString().trim());

            Toast.makeText(this,
                    result ? "Student deleted" : "Student not found",
                    Toast.LENGTH_SHORT).show();
        });

        view.setOnClickListener(v -> {
            Cursor cursor = database.viewStudents();
            StringBuilder result = new StringBuilder();

            while (cursor.moveToNext()) {
                result.append("Name: ")
                        .append(cursor.getString(1))
                        .append("\nID: ")
                        .append(cursor.getString(0))
                        .append("\nAge: ")
                        .append(cursor.getInt(2))
                        .append("\n\n");
            }

            cursor.close();

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Student Records")
                    .setMessage(result.length() == 0
                            ? "No students found"
                            : result.toString())
                    .setPositiveButton("OK", null)
                    .show();
        });
    }
}
