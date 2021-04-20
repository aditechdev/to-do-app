package in.aditya.letsdoit.model;



public class RVModel{
    private int id, status;
    private String date, time, task;

    public RVModel(int id, int status, String date, String time, String task) {
        this.id = id;
        this.status = status;
        this.date = date;
        this.time = time;
        this.task = task;
    }

    public RVModel() {

    }


    @Override
    public String toString() {
        return "RVModel{" +
                "id=" + id +
                ", status=" + status +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", task='" + task + '\'' +
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
}
