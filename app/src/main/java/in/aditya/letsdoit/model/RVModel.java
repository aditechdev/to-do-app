package in.aditya.letsdoit.model;


public class RVModel {
    private int id, status, alarm;
    private String date, time, task, datetime;

    public RVModel(int id, int status, int alarm, String date, String time, String task, String datetime) {
        this.id = id;
        this.status = status;
        this.alarm = alarm;
        this.date = date;
        this.time = time;
        this.task = task;
        this.datetime = datetime;
    }

    public RVModel() {

    }

    @Override
    public String toString() {
        return "RVModel{" +
                "id=" + id +
                ", status=" + status +
                ", alarm=" + alarm +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", task='" + task + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}