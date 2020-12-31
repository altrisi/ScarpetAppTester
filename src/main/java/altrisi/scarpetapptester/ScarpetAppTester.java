package altrisi.scarpetapptester.ScarpetAppTester;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.script.CarpetExpression;
import carpet.settings.SettingsManager;
import carpet.utils.Messenger;

import java.util.Map;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class ScarpetAppTester implements CarpetExtension, ModInitializer
{
    private static SettingsManager settingsManager;
    
    @Override
    public void onInitialize()
    {
    	mySettingsManager = new SettingsManager("1.0", "examplemod", "Example Mod");
        CarpetServer.manageExtension(this);
    }

    @Override
    public void onGameStarted()
    {
        mySettingsManager.parseSettingsClass(ScarpetAppTesterSettings.class);

    @Override
    public String version() { return "scarpet-app-tester" }

    @Override
    public void onServerLoaded(MinecraftServer server)
    {
        // reloading of /carpet settings is handled by carpet
        // reloading of own settings is also handled by Carpet as an extension, since we claim own settings manager at customSettingsManager()
    	// You can remove the events you don't use
    }
    
    @Override
    public void onServerLoadedWorlds(MinecraftServer server) {
    	// Happens when the server has loaded itself AND the worlds
    	// You can remove this event if you don't need it
    }
    
    @Override
    public void onReload(MinecraftServer server) {
    	// Happens when the server reloads (usually /reload command)
    	// You can remove this event if you don't need it
    }
    
    @Override
    public void onServerClosed(MinecraftServer server) {
    	// Happens when the server closes. Can happen multiple times in singleplayer
    	// You can remove this event if you don't need it
    }

    @Override
    public void onTick(MinecraftServer server)
    {
        // no need to add this.
    }

    @Override
    public SettingsManager customSettingsManager()
    {
        return mySettingsManager;
    }

    @Override
    public void onPlayerLoggedIn(ServerPlayerEntity player)
    {
        // You can remove this event if you don't need it
    }

    @Override
    public void onPlayerLoggedOut(ServerPlayerEntity player)
    {
        // You can remove this event if you don't need it
    }
    
    @Override
    public void scarpetApi(CarpetExpression expression) {
    	// Use this to add your own methods to Scarpet
    	// Check javadocs for more details
    	// As with everything, you can remove this if you don't need it
    }
}
