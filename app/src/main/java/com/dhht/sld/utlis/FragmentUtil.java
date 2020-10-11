package com.dhht.sld.utlis;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtil {

    public static void replace(FragmentManager manager, int fgmId, Fragment to, String tag) {
        FragmentTransaction transaction = manager.beginTransaction();
        //遍历隐藏所有添加的fragment
        for (Fragment fragment : manager.getFragments()) {
            transaction.hide(fragment);
        }
        if (!to.isAdded()) { //若没有添加过
            transaction.add(fgmId, to, tag).commit();
        } else { //若已经添加
            transaction.show(to).commit();
        }
    }

    public void replaceAn()
    {
        /*
        FragmentTransaction transaction = manager.beginTransaction();
        if (type == 1)
        {
            transaction.setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out)
                    .replace(R.id.main_bottom_head, new HelpPeopleBottomFragment(), "HelpPeopleBottomFragment");
        }else{
            transaction.setCustomAnimations(R.anim.slide_left_in,R.anim.slide_right_out)
                    .replace(R.id.main_bottom_head, new FindPeopleBottomFragment(),"FindPeopleBottomFragment");
        }
        */
    }




}
