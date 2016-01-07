package opengl.glexamples;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Angel on 15/12/26.
 */
public class UserEntity implements Parcelable{
    private String id;
    private String name;
    private String phone;
    private String email;
    private String category;
    private String style;

    public UserEntity(){}
    UserEntity(String id,String name,String phone,String email,String category,String style){
        this.id=id;
        this.name=name;
        this.phone=phone;
        this.email=email;
        this.category=category;
        this.style=style;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(category);
        dest.writeString(style);
    }



    public static final Parcelable.Creator<UserEntity> CREATOR
            = new Parcelable.Creator<UserEntity>() {
        public UserEntity createFromParcel(Parcel in) {
            return new UserEntity(in);
        }

        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };

    private UserEntity(Parcel in) {
        id=in.readString();
        name = in.readString();
        phone=in.readString();
        email=in.readString();
        category=in.readString();
        style=in.readString();
    }
}
