package com.example.fyp_anroid;

public class doc_list {
    private String id;

    private String name_eng;
    private String name_chi;
    private String location;
    private String mark;
    private Boolean Medical;
    public doc_list(String id, String name_eng,String name_chi,String location,String mark,Boolean Medical) {
        this.id = id;
        this.name_eng = name_eng;
        this.name_chi = name_chi;
        this.location = location;
        this.mark = mark;
        this.Medical = Medical;
    }
    public Boolean getMedical(){
        return Medical;
    }

    public void setMedical(Boolean medical) {
        Medical = medical;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_eng() {
        return name_eng;
    }

    public void setName_chi(String name_chi) {
        this.name_chi = name_chi;
    }
    public String getName_chi() {
        return name_chi;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation() {
        return location;
    }

    public void setName_eng(String name_eng) {
        this.name_eng = name_eng;
    }
    public String getMark(){
        return this.mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
