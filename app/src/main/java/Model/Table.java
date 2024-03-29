package Model;

public class Table {
    private String id;
    private String name;
    private String floorId;

    public Table() {
    }

    public Table(String id, String name, String floorId) {
        this.id = id;
        this.name = name;
        this.floorId = floorId;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
