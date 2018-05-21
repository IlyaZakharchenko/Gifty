package ru.kpfu.itis.gifty.model.listeners;

import ru.kpfu.itis.gifty.model.entities.User;

/**
 * Created by Ilya Zakharchenko on 17.05.2018.
 */
public interface UserProviderOnCompleteListener {

    void onComplete(User user);
}
