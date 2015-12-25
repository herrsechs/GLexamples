package opengl.glexamples;

/**
 * Created by Angel on 15/12/26.
 */
public class UserEntity {
    private String name;
    private String phone;
    private String email;
    private String group;

    public UserEntity(){}
    UserEntity(String name,String phone,String email,String group){
        this.name=name;
        this.phone=phone;
        this.email=email;
        this.group=group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
