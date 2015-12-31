package opengl.glexamples;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Angel on 15/12/26.
 */
public class UserEntity implements Parcelable{
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(group);
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
        name = in.readString();
        phone=in.readString();
        email=in.readString();
        group=in.readString();
    }
}
