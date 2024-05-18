package com.sdi.hostedin.data.model;

public class Publication {
    private String _id;
    private String title;
    private String description;
    private Accommodation accommodation;
    private User user;

    public Publication() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Publication{" +
                "_id='" + _id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", accommodation=" + (accommodation != null ? accommodation.toString() : "null") +
                ", user=" + (user != null ? user.toString() : "null") +
                '}';
    }
}
