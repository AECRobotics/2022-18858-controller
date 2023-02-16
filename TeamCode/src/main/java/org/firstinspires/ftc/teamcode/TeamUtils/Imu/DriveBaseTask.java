package org.firstinspires.ftc.teamcode.TeamUtils.Imu;

import java.util.HashMap;

public class DriveBaseTask {
    public enum TaskType {
        DRIVE_DISTANCE,
        STRAFE_DISTANCE,
        TURN_DEGREES, //I have no idea how I am going to make this work properly, probably through trial and error but through math would be very nice
        //COMBINED, //I have no idea how I am going to implement this but I'm sure it'll be full of spaghetti
        DRIVE_TO_DISTANCE_SENSOR_LESS_THAN,
        STRAFE_TO_DISTANCE_SENSOR_LESS_THAN,
        DRIVE_TO_DISTANCE,
        STRAFE_TO_DISTANCE,
        WAIT_FOR,
        PLACEHOLDER
    }
    public enum TaskState {
        EXISTING,
        STARTED,
        FINISHED
    }

    private TaskType type;
    private TaskState state;
    private HashMap<String, Double> parameters;

    public DriveBaseTask(TaskType type, HashMap<String, Double> parameters) {
        this.type = type;
        this.state = TaskState.EXISTING;
        this.parameters = parameters;
    }

    public HashMap<String, Double> getParameters() {
        return this.parameters;
    }

    public TaskType getTaskType() {
        return this.type;
    }

    public void startTask() {
        this.state = TaskState.STARTED;
    }

    public void finishTask() {
        this.state = TaskState.FINISHED;
    }

    public TaskState getState() {
        return this.state;
    }
}
