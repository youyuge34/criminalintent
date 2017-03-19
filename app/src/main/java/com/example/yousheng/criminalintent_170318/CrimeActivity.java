package com.example.yousheng.criminalintent_170318;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

//在第11章节启用此活动，建立新的CrimePagerActivity代替
//在第11章节启用此活动，建立新的CrimePagerActivity代替
//在第11章节启用此活动，建立新的CrimePagerActivity代替
//在第11章节启用此活动，建立新的CrimePagerActivity代替
//在第11章节启用此活动，建立新的CrimePagerActivity代替


public class CrimeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
//        return new CrimeFragment();
        UUID crimeId= (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        //用fragment的argument传数据，所以要用以下方法建立fragment实例
        return CrimeFragment.newInstance(crimeId);
    }

    private static final String EXTRA_CRIME_ID="crime_from_crimelistfragment_to_crimeactivity";

    //用此方法启动此活动,intent中已接受到crime实例的id信息，从而可以知道是哪个crime，然而需要id信息的是crimefragment
    public static Intent newIntent(Context context, UUID crimeId){
        Intent intent=new Intent(context,CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        return intent;
    }


    //以下这些初始化方法都封装进了父类中，减少代码重复

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fragment);
//
//        //activity类中添加了fragmentmanger类，负责管理fragment并将它们的视图添加到activity的视图层级结构中
//        //manger类具体管理：fragment队列和fragment事务回退栈
//        FragmentManager manager=getSupportFragmentManager();
//
//        //向manger请求获取fragment，若要获取的fragment已存在于队列中，manger就直接返回它
//        //因为旋转或内存回收后会销毁activity，重建时会调用activity.oncreate()方法。
//        //activity被销毁时，manger会将fragment队列保存下来，下次activity重建时会首先获取保存的队列并重建队列
//        Fragment fragment=manager.findFragmentById(R.id.fragment_container);
//
//        //若指定容器视图资源id的fragment不在队列中，就新建一个
//        //事务用来管理fragment队列中的fragment，而manger管理fragment事务回退栈。
//        if(fragment==null){
//            fragment=new CrimeFragment();
//            //manger的开启事务方法得到事务的实例，添加操作并提交事务
//            manager.beginTransaction()
//                    /*
//                    * add方法有两个作用，一个是告诉manger，fragment应出现在哪里
//                    * 二是将资源id用作队列中fragment的唯一标识，所以想获取某个fragment，
//                    * 只要使用捆绑的资源id就行（如上放的manger.findFragmentById方法）
//                     */
//                    .add(R.id.fragment_container,fragment)
//                    .commit();
//
//        }
//    }
}
