package ru.aiefu.fabricrestart.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Util;
import ru.aiefu.fabricrestart.FabricRestart;

public class RestartCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(CommandManager.literal("restart").requires(source -> source.hasPermissionLevel(4)).executes(context ->
                execute(context.getSource())).then(CommandManager.literal("delay")
                .then(CommandManager.argument("time", IntegerArgumentType.integer()).executes(context ->
                        delayRestart(context.getSource(),IntegerArgumentType.getInteger(context, "time"))))));
    }

    private static int execute(ServerCommandSource source){
        source.getMinecraftServer().stop(false);
        return 0;
    }

    private static int delayRestart(ServerCommandSource source, int minutes){
        long delaytime = (long) minutes * 60 * 1000;
        FabricRestart.RESTART_TIME += delaytime;
        if(delaytime >= 16000){
            FabricRestart.timer = 0;
            FabricRestart.timer2 = 15;
        }
        if(delaytime >= 60000){
            FabricRestart.secondPrint = false;
        }
        if(delaytime >= 300000){
            FabricRestart.firstPrint = false;
        }
        source.getMinecraftServer().getPlayerManager().getPlayerList().forEach(player ->
                player.sendSystemMessage(new LiteralText("Restart has been delayed by " + minutes + " minutes"), Util.NIL_UUID));
        return 0;
    }
}
