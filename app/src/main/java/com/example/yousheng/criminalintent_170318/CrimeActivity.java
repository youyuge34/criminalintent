package com.example.yousheng.criminalintent_170318;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class CrimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);

        //activity类中添加了fragmentmanger类，负责管理fragment并将它们的视图添加到activity的视图层级结构中
        //manger类具体管理：fragment队列和fragment事务回退栈
        FragmentManager manager=getSupportFragmentManager();

        //向manger请求获取fragment，若要获取的fragment已存在于队列中，manger就直接返回它
        //因为旋转或内存回收后会销毁activity，重建时会调用activity.oncreate()方法。
        //activity被销毁时，manger会将fragment队列保存下来，下次activity重建时会首先获取保存的队列并重建队列
        Fragment fragment=manager.findFragmentById(R.id.fragment_container);

        //若指定容器视图资源id的fragment不在队列中，就新建一个
        //事务用来管理fragment队列中的fragment，而manger管理fragment事务回退栈。
        if(fragment==null){
            fragment=new CrimeFragment();
            //manger的开启事务方法得到事务的实例，添加操作并提交事务
            manager.beginTransaction()
                    /*
                    * add方法有两个作用，一个是告诉manger，fragment应出现在哪里
                    * 二是将资源id用作队列中fragment的唯一标识，所以想获取某个fragment，
                    * 只要使用捆绑的资源id就行（如上放的manger.findFragmentById方法）
                     */
                    .add(R.id.fragment_container,fragment)
                    .commit();

        }
    }
}
