package ru.kpfu.itis.gifty.model.entities;

import com.google.firebase.firestore.Exclude;
import java.util.ArrayList;
import java.util.List;
import ru.kpfu.itis.gifty.model.providers.UserProvider;

/**
 * Created by Ilya Zakharchenko on 11.05.2018.
 */
public class Gift {

    private String checkedFriendUid;
    private List<Group> groupList;
    private List<String> groupsId;
    private String name;

    public Gift() {

    }

    public Gift(String name) {
        checkedFriendUid = "";
        groupList = new ArrayList<>();
        groupsId = new ArrayList<>();
        this.name = name;
    }

    private void parceGroupList() {
        groupList = new ArrayList<>();
        if (groupsId != null) {
            List<Group> groups = UserProvider.getInstance().getUser().getGroupList();
            for (Group group : groups) {
                if (groupsId.contains(group.getName())) {
                    groupList.add(group);
                }
            }
        }
    }

    public String getCheckedFriendUid() {
        return checkedFriendUid;
    }

    public void setCheckedFriendUid(String checkedFriendUid) {
        this.checkedFriendUid = checkedFriendUid;
    }

    @Exclude
    public List<Group> getGroupList() {
        parceGroupList();
        return groupList;
    }

    @Exclude
    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public List<String> getGroupsId() {
        return groupsId;
    }

    public void setGroupsId(List<String> groupsId) {
        this.groupsId = groupsId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
