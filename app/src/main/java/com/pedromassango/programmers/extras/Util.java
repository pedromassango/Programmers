package com.pedromassango.programmers.extras;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.pedromassango.programmers.BuildConfig;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.models.Usuario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.pedromassango.programmers.extras.Constants.AcountStatus.INCOMPLETE;

/**
 * Created by Pedro Massango on 21-11-2016 at 21:35.
 */

public class Util {

    // TIME AGO
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private static ArrayList<Usuario> users;
    private static ArrayList<Post> _posts;

    public static String getFirstName(String name) {
        return name.contains(" ") ?
                name.substring(0, name.indexOf(" ") + 1)
                :
                name;
    }

    public static String concat(Object... args) {
        String joineed = "";
        for (Object s : args) {
            joineed = joineed + " " + s;
        }
        return joineed;
    }

    public static String join(Object... args) {
        String joineed = "";
        for (Object s : args) {
            joineed = joineed + "" + s;
        }
        return joineed;
    }

    public static String getTimeAgo(long time) {

        String MIN = " min";
        String HR = "h";
        String HA = "há";

        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time <= 0) {
            return null;
        } else if (time > now) {
            time = now;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "agora";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return concat(HA, join("1 ", MIN));
        } else if (diff < 50 * MINUTE_MILLIS) {
            return concat(HA, join((diff / MINUTE_MILLIS), MIN));
        } else if (diff < 90 * MINUTE_MILLIS) {
            return concat(HA, join("1 ", HR));
        } else if (diff < 24 * HOUR_MILLIS) {
            return concat(HA, join((diff / HOUR_MILLIS), HR));
        } else if (diff < 48 * HOUR_MILLIS) {
            return concat(HA, "1 d");
        } else if ((diff / DAY_MILLIS) > 30) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(time);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH) + 1;
            int year = c.get(Calendar.YEAR);
            return String.format("%s/%s/%s", day, month, year);
        } else {
            return concat(HA, join((diff / DAY_MILLIS), "d"));
        }
    }

    public static boolean getTimeState(long time) {
        boolean output = false;

        Date last = new Date(time);
        Date current = new Date(System.currentTimeMillis());

        output = last.getYear() < current.getYear() ||
                last.getMonth() < current.getMonth() ||
                last.getDay() < current.getDay() ||
                last.getHours() < current.getHours() ||
                last.getMinutes() < current.getMinutes();

        return output;
    }

    public static ArrayList<Post> getPosts() {
        if (null == _posts) {
            _posts = new ArrayList<>();
            Usuario usuario = new Usuario();

            for (int i = 0; i < 20; i++) {
                if (i == 11 || i == 8 || i == 10) {
                    usuario = new Usuario();
                    usuario.setEmail("usuario2@gmail.com");
                    usuario.setUsername("José Eduardo");
                }

                if (i == 2 || i == 4 || i == 6) {
                    usuario = new Usuario();
                    usuario.setEmail("usuario2@gmail.com");
                    usuario.setUsername("José Eduardo");
                }

                if (i == 9 || i == 7 || i == 14) {
                    usuario = new Usuario();
                    usuario.setEmail("usuario2@gmail.com");
                    usuario.setUsername("André Mendes");
                }

                if (i == 12 || i == 14 || i == 19) {
                    usuario = new Usuario();
                    usuario.setEmail("usuario1@gmail.com");
                    usuario.setUsername("Angelo Garcia");
                }

                if (i == 1 || i == 13 || i == 15) {
                    usuario = new Usuario();
                    usuario.setEmail("usuario4@gmail.com");
                    usuario.setUsername("Anisio Isidoro");
                }


                if (i == 3 || i == 5 || i == 8 || i == 1 || i == 10) {
                    usuario = new Usuario();
                    usuario.setEmail("usuario@gmail.com");
                    usuario.setUsername("Pedro Massango");
                }

                if (null == usuario.getUsername()) {

                    usuario.setEmail("usuario" + i + "@gmail.com");
                    usuario.setUsername("Pedro Massango " + i);
                }


                Post post = new Post();
                post.setAuthorId(usuario.getId());
                post.setCommentsActive(true);
                post.setAuthor(usuario.getUsername());
                post.setTitle("Como usar parámetros do AsyncTask em java no Android?");
                post.setCategory("Java");
                post.setBody("Fala pessoal, como eu vou usar os parámetros do AsyncTask no Android? quando eu vou criar a classe que estande dela,ele pede três paramentro, e nao entendo como eles funcionam e qual a ordem deles. Obrigado");
                post.setId(String.valueOf(i));

                post.setViews(2 * i);

                post.setTimestamp(System.currentTimeMillis());
                _posts.add(post);
            }
        }

        return _posts;
    }

    public static ArrayList<Comment> getComments() {
        ArrayList<Comment> comments = new ArrayList<>();
        for (int x = 0; x < 20; x++) {
            Comment comment = new Comment();
            comment.setPostId("64545");
            comment.setAuthor("Pedro Massango");
            comment.setAuthorId("usuario@gmail.com");
            comment.setAuthorUrlPhoto("ww.url.com");
            comment.setId(String.valueOf(123 + x));
            comment.setTimestamp(System.currentTimeMillis());
            comment.setText("Fala Pedro, resoleu já sua dúvida?");
            comment.setVotes(new HashMap<String, Boolean>());

            comments.add(comment);
        }
        return comments;
    }

    public static ArrayList<Usuario> getUsers() {
        if (null == users) {
            users = new ArrayList<>(10);
            for (int i = 0; i < 10; i++) {

                Usuario usuario = new Usuario();
                usuario.setId("16wet712611ggy2");
                usuario.setEmail("pedro@gmail.com");
                usuario.setUsername("Usuário " + i);
                usuario.setReputation(40 + i);
                usuario.setCity("Luanda");
                usuario.setProgrammingLanguage("Java");
                usuario.setCodeLevel("Iniciante");
                users.add(usuario);
            }
        }
        return users;
    }

    public static Usuario getSignupUser(String username, String email, String password) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setReputation(15);
        usuario.setAccountComplete(INCOMPLETE);
        return usuario;
    }

    public static Usuario getUser() {
        Usuario usuario = new Usuario();
        usuario.setEmail("pedro@gmail.com");
        usuario.setUsername("Usuário ");
        usuario.setReputation(40);
        usuario.setUrlPhoto("");
        usuario.setCity("Luanda");
        usuario.setProgrammingLanguage("Java");
        usuario.setCodeLevel("Iniciante");
        return usuario;
    }

    public static void showLog(Object code, String message) {
        String log = concat(code, message);
        Log.v("www", log);
    }

    public static void showLog(String message) {
        Log.v("output", "\nMESSAGE: " + message);
    }

    public static void showAlertDialog(Context context, String mTitle, String mMessage) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(mTitle)
                .setMessage(mMessage)
                .setPositiveButton(context.getString(R.string.str_ok), null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showToast(Context context, Object message) {
        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String getError(Exception exception) {

        return exception.getMessage();
    }

    static boolean validImageUrl(String imgUrl) {
        return imgUrl != null && imgUrl.trim().length() > 0;
    }

    public static void killAppProccess() {
        int appProccessId = Process.myPid();
        Process.killProcess(appProccessId);
    }

    public static boolean isAppRunning(final Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(BuildConfig.APPLICATION_ID)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static long getTime() {
        return System.currentTimeMillis();
    }
}
