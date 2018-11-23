package com.netposa.common.inject;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.netposa.common.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import androidx.fragment.app.Fragment;

/**
 * Created by yexiaokang on 2017/11/3.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public final class InjectUtils {

    private static final String TAG = "InjectUtils";
    private static final boolean LOG_DEBUG = false;

    public static void inject(Activity activity) {
        injectViews(activity);
        injectOnClick(activity);
    }

    public static void injectViews(Activity activity) {
        try {
            if (LOG_DEBUG) {
                Log.i(TAG, "injectViews: " + activity.getClass().toString());
            }
            View rootView = activity.getWindow().getDecorView();
            Field[] fields = activity.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isFinal(field.getModifiers()) ||
                        Modifier.isStatic(field.getModifiers())) {
                    // ignore final static field
                    continue;
                }
                if (!View.class.isAssignableFrom(field.getType())) {
                    // ignore not view field
                    continue;
                }
                if (LOG_DEBUG) {
                    Log.i(TAG, "injectViews: " + field.getName() + "/" + field.getType().toString());
                }
                if (field.isAnnotationPresent(InjectView.class)) {
                    InjectView injectView = field.getAnnotation(InjectView.class);
                    int id = injectView.id();
                    String idString = injectView.idString();
                    id = findViewId(id, activity, idString);
                    if (id != View.NO_ID) {
                        View view = rootView.findViewById(id);
                        if (view != null) {
                            field.setAccessible(true);
                            field.set(activity, view);
                            if (LOG_DEBUG) {
                                Log.i(TAG, "injectViews: set " + field.getName() + " view = " +
                                        view.getClass().toString());
                            }
                        } else {
                            Log.w(TAG, "injectViews: can't find view");
                        }
                    } else {
                        Log.w(TAG, "injectViews: can't find view id, idString = " + idString);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void injectOnClick(Activity activity) {
        if (LOG_DEBUG) {
            Log.i(TAG, "injectOnClick: " + activity.getClass().toString());
        }
        View rootView = activity.getWindow().getDecorView();
        Method[] methods = activity.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (Modifier.isFinal(method.getModifiers()) ||
                    Modifier.isAbstract(method.getModifiers()) ||
                    Modifier.isStatic(method.getModifiers())) {
                // ignore final abstract static method
                continue;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1 || parameterTypes[0] != View.class) {
                // parameter must be matched
                continue;
            }
            if (method.getReturnType() != void.class) {
                // return type must be matched
                continue;
            }
            if (LOG_DEBUG) {
                Log.i(TAG, "injectOnClick: " + method.getName());
            }
            if (method.isAnnotationPresent(InjectOnClick.class)) {
                InjectOnClick injectOnClick = method.getAnnotation(InjectOnClick.class);
                int ids[] = injectOnClick.ids();
                String[] idStrings = injectOnClick.idStrings();
                if (LOG_DEBUG) {
                    Log.i(TAG, "injectOnClick: idStrings = " + Arrays.toString(idStrings));
                }
                OnClickProxy<Activity> proxy = new OnClickProxy<>(activity, method);
                int[] newIds = findViewId(ids, activity, idStrings);
                for (int id : newIds) {
                    if (id != View.NO_ID) {
                        View view = rootView.findViewById(id);
                        if (view != null) {
                            if (LOG_DEBUG) {
                                Log.i(TAG, "injectOnClick: set " + method.getName() +
                                        " as view click listener");
                            }
                            view.setOnClickListener(proxy);
                        } else {
                            Log.w(TAG, "injectOnClick: can't find view");
                        }
                    } else {
                        Log.w(TAG, "injectOnClick: can't find view id");
                    }
                }
            }
        }

    }

    public static void inject(Fragment fragment) {
        injectViews(fragment);
        injectOnClick(fragment);
    }

    public static void injectViews(Fragment fragment) {
        try {
            if (LOG_DEBUG) {
                Log.i(TAG, "injectViews: " + fragment.getClass().toString());
            }
            View rootView = fragment.getView();
            if (rootView == null) {
                Log.w(TAG, "injectViews: rootView == null");
                return;
            }
            Field[] fields = fragment.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isFinal(field.getModifiers()) ||
                        Modifier.isStatic(field.getModifiers())) {
                    // ignore final static field
                    continue;
                }
                if (!View.class.isAssignableFrom(field.getType())) {
                    // ignore not view field
                    continue;
                }
                if (LOG_DEBUG) {
                    Log.i(TAG, "injectViews: " + field.getName() + "/" + field.getType().toString());
                }
                if (field.isAnnotationPresent(InjectView.class)) {
                    InjectView injectView = field.getAnnotation(InjectView.class);
                    int id = injectView.id();
                    String idString = injectView.idString();
                    id = findViewId(id, fragment.getActivity(), idString);
                    if (id != View.NO_ID) {
                        View view = rootView.findViewById(id);
                        if (view != null) {
                            field.setAccessible(true);
                            field.set(fragment, view);
                            if (LOG_DEBUG) {
                                Log.i(TAG, "injectViews: set " + field.getName() + " view = " +
                                        view.getClass().toString());
                            }
                        } else {
                            Log.w(TAG, "injectViews: can't find view");
                        }
                    } else {
                        Log.w(TAG, "injectViews: can't find view id, idString = " + idString);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void injectOnClick(Fragment fragment) {
        if (LOG_DEBUG) {
            Log.i(TAG, "injectOnClick: " + fragment.getClass().toString());
        }
        View rootView = fragment.getView();
        if (rootView == null) {
            Log.w(TAG, "injectOnClick: rootView == null");
            return;
        }
        Method[] methods = fragment.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (Modifier.isFinal(method.getModifiers()) ||
                    Modifier.isAbstract(method.getModifiers()) ||
                    Modifier.isStatic(method.getModifiers())) {
                // ignore final abstract static method
                continue;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1 || parameterTypes[0] != View.class) {
                // parameter must be matched
                continue;
            }
            if (method.getReturnType() != void.class) {
                // return type must be matched
                continue;
            }
            if (LOG_DEBUG) {
                Log.i(TAG, "injectOnClick: " + method.getName());
            }
            if (method.isAnnotationPresent(InjectOnClick.class)) {
                InjectOnClick injectOnClick = method.getAnnotation(InjectOnClick.class);
                int ids[] = injectOnClick.ids();
                String[] idStrings = injectOnClick.idStrings();
                if (LOG_DEBUG) {
                    Log.i(TAG, "injectOnClick: idStrings = " + Arrays.toString(idStrings));
                }
                OnClickProxy<Fragment> proxy = new OnClickProxy<>(fragment, method);
                int[] newIds = findViewId(ids, fragment.getActivity(), idStrings);
                for (int id : newIds) {
                    if (id != View.NO_ID) {
                        View view = rootView.findViewById(id);
                        if (view != null) {
                            if (LOG_DEBUG) {
                                Log.i(TAG, "injectOnClick: set " + method.getName() +
                                        " as view click listener");
                            }
                            view.setOnClickListener(proxy);
                        } else {
                            Log.w(TAG, "injectOnClick: can't find view");
                        }
                    } else {
                        Log.w(TAG, "injectOnClick: can't find view id");
                    }
                }
            }
        }

    }

    private static int[] findViewId(int[] ids, Context context, String[] idStrings) {
        int count = ids.length + idStrings.length;
        int[] newIds = Arrays.copyOf(ids, count);
        if (idStrings.length > 0) {
            int[] tmp = new int[idStrings.length];
            for (int i = 0; i < idStrings.length; i++) {
                tmp[i] = findViewId(context, idStrings[i]);
            }
            System.arraycopy(tmp, 0, newIds, ids.length, idStrings.length);
        }
        return newIds;
    }

    private static int findViewId(int id, Context context, String idString) {
        if (id != View.NO_ID) {
            return id;
        }
        return findViewId(context, idString);
    }

    private static int findViewId(Context context, String idString) {
        if (context == null) {
            return View.NO_ID;
        }
        if (TextUtils.isEmpty(idString)) {
            return View.NO_ID;
        }
        int id = 0;
        try {
            Class res = R.id.class;
            Field field = res.getField(idString);
            id = field.getInt(null);
        } catch (Exception ignored) {
        }
        if (id == 0) {
            if (LOG_DEBUG) {
                Log.i(TAG, "findViewId: can't find id by R.id.class, try Resources#getIdentifier|" +
                        context.getPackageName());
            }
            id = context.getResources().getIdentifier(idString, "id", context.getPackageName());
        }
        if (id != 0) {
            return id;
        } else {
            Log.w(TAG, "Could not find id of \"" + idString + "\"");
        }
        return View.NO_ID;
    }

    private static class OnClickProxy<T> implements View.OnClickListener {

        private WeakReference<T> mReference;
        private Method mMethod;

        OnClickProxy(T reference, Method method) {
            mReference = new WeakReference<>(reference);
            mMethod = method;
        }

        @Override
        public void onClick(View v) {
            if (mReference.get() != null) {
                try {
                    mMethod.setAccessible(true);
                    mMethod.invoke(mReference.get(), v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
