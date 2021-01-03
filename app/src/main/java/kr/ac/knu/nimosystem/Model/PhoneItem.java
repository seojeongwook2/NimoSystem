package kr.ac.knu.nimosystem.Model;

public class PhoneItem {
    private String number;
    private String name;
    private String _id;

    public PhoneItem(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public PhoneItem(String number, String name, String _id) {
        this.number = number;
        this.name = name;
        this._id = _id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
