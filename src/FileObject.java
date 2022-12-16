public class FileObject {
    private String name;
    private boolean hidden;
    private double size;

    FileObject(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
