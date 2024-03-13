package cz.spsmb.dto;

public class SongDTO {
    private String name;
    private String author;
    private String album;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    @Override
    public String toString() {
        return "SongDTO{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", album='" + album + '\'' +
                '}';
    }
}
