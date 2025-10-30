package net.aros.pways.client.openutil;

import net.aros.pways.Pways;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class ProcessLauncher {
    static void launchProcess(PlayerEntity player, String programPath, List<Identifier> assetPaths, boolean blockbench) {
        if (areAssetsMissing(assetPaths, player) || isProgramPathBlank(programPath, player) || doesProgramNotExist(programPath, player))
            return;
        List<String> arguments = buildProcessArguments(player, programPath, assetPaths);
        startExternalProcess(player, arguments, blockbench);
    }

    private static boolean areAssetsMissing(@NotNull List<Identifier> assetPaths, PlayerEntity player) {
        if (!assetPaths.isEmpty()) return false;
        sendErrorToPlayer(player, "assets_not_found");
        return true;
    }

    private static boolean isProgramPathBlank(@NotNull String programPath, PlayerEntity player) {
        if (!programPath.isBlank()) return false;
        sendErrorToPlayer(player, "program_is_empty");
        return true;
    }

    private static boolean doesProgramNotExist(String programPath, PlayerEntity player) {
        if (Files.exists(Paths.get(programPath))) return false;
        sendErrorToPlayer(player, "program_not_found");
        return true;
    }

    private static @NotNull List<String> buildProcessArguments(PlayerEntity player, String programPath, @NotNull List<Identifier> assetPaths) {
        List<String> arguments = new ArrayList<>();
        arguments.add(programPath);
        for (Identifier path : assetPaths) {
            arguments.add(getAssetPath(player, path));
        }
        return arguments;
    }

    private static @NotNull String getAssetPath(PlayerEntity player, @NotNull Identifier assetPath) {
        Path extractedPath = ResourceExtractor.extractToTemporaryPack(player, assetPath.getNamespace())
                .resolve(assetPath.getPath()).toAbsolutePath();
        return extractedPath.toString();
    }

    private static void startExternalProcess(PlayerEntity player, List<String> arguments, boolean blockbench) {
        try {
            if (blockbench)
                openBlockbench(arguments);
            else
                startProcess(arguments);
        } catch (IOException e) {
            sendErrorToPlayer(player, "process_error");
            Pways.LOGGER.info("Process start failed. Arguments: {}", arguments, e);
        }
    }

    private static void openBlockbench(@NotNull List<String> arguments) throws IOException {
        String program = arguments.getFirst();
        for (String arg : arguments.stream().skip(1).toList()) {
            startProcess(List.of(program, arg));
        }
    }

    private static void sendErrorToPlayer(@NotNull PlayerEntity player, String error) {
        player.sendMessage(Text.translatable("msg.%s.%s".formatted(Pways.MOD_ID, error)).formatted(Formatting.RED, Formatting.ITALIC));
    }

    private static void startProcess(List<String> arguments) throws IOException {
        new ProcessBuilder(arguments)
                .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                .redirectError(ProcessBuilder.Redirect.DISCARD)
                .start();
    }
}