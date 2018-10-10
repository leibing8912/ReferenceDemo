package com.yy.mobile.referencedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private SoftReference<Object> mSoftReference;
    private WeakReference<Object> mWeakReference;
    private PhantomReference<Object> mPhantomReference;
    private ReferenceQueue mSoftReferenceQueue;
    private ReferenceQueue mWeakReferenceQueue;
    private ReferenceQueue mPhantomReferenceQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_gc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        referenceTest();
                    }
                }).start();
            }
        });
    }

    /**
     * 引用测试
     */
    private void referenceTest() {
        mSoftReferenceQueue = new ReferenceQueue();
        mWeakReferenceQueue = new ReferenceQueue();
        mPhantomReferenceQueue = new ReferenceQueue();
        mSoftReference = new SoftReference<Object>(new TestObj(TestObj.TYPE_SOFT_REFERENCE), mSoftReferenceQueue);
        mWeakReference = new WeakReference<Object>(new TestObj(TestObj.TYPE_WEAK_REFERENCE), mWeakReferenceQueue);
        mPhantomReference = new PhantomReference<Object>(new TestObj(TestObj.TYPE_PHANTOM_REFERENCE), mPhantomReferenceQueue);
        while (true) {
            System.gc();
            System.out.println("referenceTest system gc");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("referenceTest mSoftReference = " + mSoftReference.get() + " mWeakReference = "
                    + mWeakReference.get() + " mPhantomReference = " + mPhantomReference.get());
            Reference<Object> mSoftRef = mSoftReferenceQueue.poll();
            Reference<Object> mWeakRef = mWeakReferenceQueue.poll();
            Reference<Object> mPhantomRef = mPhantomReferenceQueue.poll();
            System.out.println("referenceTest mSoftRef = " + mSoftRef + " mWeakRef = "
                    + mWeakRef + " mPhantomRef = " + mPhantomRef);
        }
    }

    /**
     * 测试对象
     */
    public static class TestObj {
        // 类型--软引用
        public final static String TYPE_SOFT_REFERENCE = "type_soft_reference";
        // 类型--弱引用
        public final static String TYPE_WEAK_REFERENCE = "type_weak_reference";
        // 类型--虚引用
        public final static String TYPE_PHANTOM_REFERENCE = "type_phantom_reference";
        // 类型
        private String curType;
        // 静态
        private static TestObj mTestObj;

        public TestObj(String curType) {
            this.curType = curType;
        }

        @Override
        protected void finalize() throws Throwable {
//            mTestObj = this;
            System.out.println("referenceTest finalize at " + new Date() + " curType = " + curType);
        }
    }
}
