package kr.ac.knu.nimosystem.Model;

public class LoginItem {
    private String time;
    private String type;

    public LoginItem(String time, String type) {
        this.time = time;
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
