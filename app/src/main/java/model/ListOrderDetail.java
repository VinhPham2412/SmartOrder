package model;

public class ListOrderDetail {
    private OrderDetail details;
    private String id;

    public ListOrderDetail() {
    }

    public ListOrderDetail(OrderDetail details, String id) {
        this.details = details;
        this.id = id;
    }

    public OrderDetail getList() {
        return details;
    }

    public void setList(OrderDetail details) {
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
