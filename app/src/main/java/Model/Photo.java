package Model;

public class Photo {
    private int resourceId;

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public Photo(int resourceId) {
        this.resourceId = resourceId;
    }
}
