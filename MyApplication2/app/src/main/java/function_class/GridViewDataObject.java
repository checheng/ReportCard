package function_class;

/**
 * Created by Administrator on 2017/3/2.
 */

public class GridViewDataObject {
    private String name;
    private String value;

    public GridViewDataObject(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
