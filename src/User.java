public class User {
    private String name;
    private boolean admin;

    User(String name, boolean admin){
        this.name = name;
        this.admin = admin;
    }

    public String getName(){
        return this.name;
    }

    public boolean getAdmin(){
        return this.admin;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
