package net.aros.pways;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.SectionHeader;

@Modmenu(modId = Pways.MOD_ID)
@Config(name = Pways.MOD_ID, wrapperName = "PwaysConfig")
public class PwaysConfigModel {
    @SectionHeader("programs")
    public String pathToImageEditor = "";
    public String pathToBlockbench = "";

    @SectionHeader("other")
    public boolean alwaysRewrite = false;
}
