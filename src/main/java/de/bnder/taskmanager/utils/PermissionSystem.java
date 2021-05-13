package de.bnder.taskmanager.utils;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import de.bnder.taskmanager.main.Main;
import de.bnder.taskmanager.utils.permissions.BoardPermission;
import de.bnder.taskmanager.utils.permissions.GroupPermission;
import de.bnder.taskmanager.utils.permissions.PermissionPermission;
import de.bnder.taskmanager.utils.permissions.TaskPermission;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.jsoup.Jsoup;

public class PermissionSystem {

    //Permission checking
    public static boolean hasPermission(Member member, TaskPermission taskPermission) {
        return checkPerms(member, taskPermission.name());
    }

    public static boolean hasPermission(Member member, PermissionPermission taskPermission) {
        return checkPerms(member, taskPermission.name());
    }

    public static boolean hasPermission(Member member, GroupPermission groupPermission) {
        return checkPerms(member, groupPermission.name());
    }

    public static boolean hasPermission(Member member, BoardPermission boardPermission) {
        return checkPerms(member, boardPermission.name());
    }

    public static boolean hasPermission(Role role, TaskPermission taskPermission) {
        return checkPerms(role, taskPermission.name());
    }

    public static boolean hasPermission(Role role, PermissionPermission taskPermission) {
        return checkPerms(role, taskPermission.name());
    }

    public static boolean hasPermission(Role role, GroupPermission groupPermission) {
        return checkPerms(role, groupPermission.name());
    }

    public static boolean hasPermission(Role role, BoardPermission boardPermission) {
        return checkPerms(role, boardPermission.name());
    }


    //Permission adding
    public static int addPermissionStatusCode(Member member, PermissionPermission taskPermission) {
        return sendAddPermRequestUser(member, taskPermission.name());
    }

    public static int addPermissionStatusCode(Role role, PermissionPermission taskPermission) {
        return sendAddPermRequestRole(role, taskPermission.name());
    }

    public static int addPermissionStatusCode(Member member, GroupPermission groupPermission) {
        return sendAddPermRequestUser(member, groupPermission.name());
    }

    public static int addPermissionStatusCode(Role role, GroupPermission taskPermission) {
        return sendAddPermRequestRole(role, taskPermission.name());
    }

    public static int addPermissionStatusCode(Member member, TaskPermission taskPermission) {
        return sendAddPermRequestUser(member, taskPermission.name());
    }

    public static int addPermissionStatusCode(Role role, TaskPermission taskPermission) {
        return sendAddPermRequestRole(role, taskPermission.name());
    }

    public static int addPermissionStatusCode(Member member, BoardPermission boardPermission) {
        return sendAddPermRequestUser(member, boardPermission.name());
    }

    public static int addPermissionStatusCode(Role role, BoardPermission boardPermission) {
        return sendAddPermRequestRole(role, boardPermission.name());
    }

    //Permission removing

    public static int removePermissionStatusCode(Member member, PermissionPermission taskPermission) {
        return sendRemovePermRequestUser(member, taskPermission.name());
    }

    public static int removePermissionStatusCode(Role role, PermissionPermission taskPermission) {
        return sendRemovePermRequestRole(role, taskPermission.name());
    }

    public static int removePermissionStatusCode(Member member, GroupPermission groupPermission) {
        return sendRemovePermRequestUser(member, groupPermission.name());
    }

    public static int removePermissionStatusCode(Role role, GroupPermission taskPermission) {
        return sendRemovePermRequestRole(role, taskPermission.name());
    }

    public static int removePermissionStatusCode(Member member, TaskPermission taskPermission) {
        return sendRemovePermRequestUser(member, taskPermission.name());
    }

    public static int removePermissionStatusCode(Role role, TaskPermission taskPermission) {
        return sendRemovePermRequestRole(role, taskPermission.name());
    }

    public static int removePermissionStatusCode(Member member, BoardPermission boardPermission) {
        return sendRemovePermRequestUser(member, boardPermission.name());
    }

    public static int removePermissionStatusCode(Role role, BoardPermission boardPermission) {
        return sendRemovePermRequestRole(role, boardPermission.name());
    }

    //Methods
    private static boolean checkPerms(Member member, String permission) {
        if (member.isOwner() || member.hasPermission(Permission.ADMINISTRATOR)) {
            return true;
        } else {
            try {
                final StringBuilder rolesBuilder = new StringBuilder();
                for (Role role : member.getRoles()) {
                    rolesBuilder.append(role.getId()).append(",");
                }
<<<<<<< Updated upstream
                final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/user/get-perms/" + member.getGuild().getId()).method(org.jsoup.Connection.Method.POST).header("authorization", "TMB " + Main.authorizationToken).header("user_id", member.getId()).data("roles", rolesBuilder.toString()).postDataCharset("UTF-8").timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
                final org.jsoup.Connection.Response res = Main.tmbAPI("user/get-perms/" + member.getGuild().getId(), member.getId(), org.jsoup.Connection.Method.POST, member.getGuild().getId()).data("roles", rolesBuilder.toString()).execute();
>>>>>>> Stashed changes
                if (res.statusCode() == 200) {
                    final JsonObject jsonObject = Json.parse(res.parse().body().text()).asObject();
                    return jsonObject.getBoolean(permission.toLowerCase(), false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean checkPerms(Role role, String permission) {
        if (role.hasPermission(Permission.ADMINISTRATOR)) {
            return true;
        } else {
            try {
<<<<<<< Updated upstream
                final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/role/get-perms/" + role.getGuild().getId()).method(org.jsoup.Connection.Method.POST).header("authorization", "TMB " + Main.authorizationToken).header("user_id", "---").data("role_id", role.getId()).postDataCharset("UTF-8").timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
                final org.jsoup.Connection.Response res = Main.tmbAPI("role/get-perms/" + role.getGuild().getId(), null, org.jsoup.Connection.Method.POST, null).data("role_id", role.getId()).execute();
>>>>>>> Stashed changes
                if (res.statusCode() == 200) {
                    final JsonObject jsonObject = Json.parse(res.parse().body().text()).asObject();
                    return jsonObject.getBoolean(permission.toLowerCase(), false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static int sendAddPermRequestRole(Role role, String name) {
        try {
<<<<<<< Updated upstream
            final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/role/add-perms/" + role.getGuild().getId()).method(org.jsoup.Connection.Method.POST).header("authorization", "TMB " + Main.authorizationToken).header("user_id", "---").data("role_id", role.getId()).data("permission", name).timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
            final org.jsoup.Connection.Response res = Main.tmbAPI("role/add-perms/" + role.getGuild().getId(), null, org.jsoup.Connection.Method.POST, null).data("role_id", role.getId()).data("permission", name).execute();
>>>>>>> Stashed changes
            return res.statusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 912;
    }

    private static int sendAddPermRequestUser(Member member, String name) {
        try {
<<<<<<< Updated upstream
            final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/user/add-perms/" + member.getGuild().getId()).method(org.jsoup.Connection.Method.POST).header("authorization", "TMB " + Main.authorizationToken).header("user_id", member.getId()).data("permission", name).timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
            final org.jsoup.Connection.Response res = Main.tmbAPI("user/add-perms/" + member.getGuild().getId(), member.getId(), org.jsoup.Connection.Method.POST, member.getGuild().getId()).data("permission", name).execute();
>>>>>>> Stashed changes
            return res.statusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 912;
    }

    private static int sendRemovePermRequestUser(Member member, String name) {
        try {
<<<<<<< Updated upstream
            final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/user/rem-perms/" + member.getGuild().getId()).method(org.jsoup.Connection.Method.POST).header("authorization", "TMB " + Main.authorizationToken).header("user_id", member.getId()).data("permission", name).timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
            final org.jsoup.Connection.Response res = Main.tmbAPI("user/rem-perms/" + member.getGuild().getId(), member.getId(), org.jsoup.Connection.Method.POST, member.getGuild().getId()).data("permission", name).execute();
>>>>>>> Stashed changes
            return res.statusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 912;
    }

    private static int sendRemovePermRequestRole(Role role, String name) {
        try {
<<<<<<< Updated upstream
            final org.jsoup.Connection.Response res = Jsoup.connect(Main.requestURL + "/role/rem-perms/" + role.getGuild().getId()).method(org.jsoup.Connection.Method.POST).header("authorization", "TMB " + Main.authorizationToken).header("user_id", "---").data("role_id", role.getId()).data("permission", name).timeout(Connection.timeout).userAgent(Main.userAgent).ignoreContentType(true).ignoreHttpErrors(true).execute();
=======
            final org.jsoup.Connection.Response res = Main.tmbAPI("role/rem-perms/" + role.getGuild().getId(), null, org.jsoup.Connection.Method.POST, null).data("role_id", role.getId()).data("permission", name).execute();
>>>>>>> Stashed changes
            return res.statusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 912;
    }

}
