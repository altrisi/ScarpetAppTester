package altrisi.scarpetapptester;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import carpet.utils.Messenger;
import net.minecraft.server.command.ServerCommandSource;

import static carpet.settings.RuleCategory.CREATIVE;

public class ExampleSimpleSettings
{
    public enum Option
    {
        OPTION_A, OPTION_B, OPTION_C
    }

    @Rule(desc = "Example integer setting", category = "misc")
    public static int intSetting = 10;

    @Rule(
            desc = "Example string type setting",
            options = {"foo", "bar", "baz"},
            extra = {
                    "This can take multiple values",
                    "that you can tab-complete in chat",
                    "but it can take any value you want"
            },
            category = "misc",
            strict = false
    )
    public static String stringSetting = "foo";

    @Rule(
            desc = "Example enum setting",
            extra = {"This is another string-type option","that conveniently parses and validates for you"},
            category = "misc")
    public static Option optionSetting = Option.OPTION_A;

    @Rule(desc = "Example bool setting", category = "misc")
    public static boolean boolSetting;
    
    /**
     *  Custom validator class for your setting. If validate returns null - settings is not changed.
     */
    private static class CheckValue extends Validator<Integer>
    {
        @Override
        public Integer validate(ServerCommandSource source, ParsedRule<Integer> currentRule, Integer newValue, String typedString)
        {
            Messenger.m(source, "rb Congrats, you just changed a setting to "+newValue);
            return newValue < 20000000 ? newValue : null;
        }
    }

    /**
     *  Simple numeric setting, no use otherwise
     */
    @Rule(
            desc = "Example numerical setting",
            options = {"32768", "250000", "1000000"},
            validate = {Validator.NONNEGATIVE_NUMBER.class, CheckValue.class},
            category = {CREATIVE, "examplemod"}
    )
    public static int uselessNumericalSetting = 32768;


    /**
     * You can define your own catergories. It makes sense to create new category for all settings in your mod.
     */
    @Rule(desc="makes mobs dance Makarena", category = {"fun", "examplemod"})
    public static boolean makarena = false;

}
