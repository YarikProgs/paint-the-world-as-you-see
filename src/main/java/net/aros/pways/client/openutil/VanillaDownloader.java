package net.aros.pways.client.openutil;

import net.aros.pways.Pways;
import net.minecraft.SharedConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

class VanillaDownloader {
    static @Nullable Path downloadVanillaAssets(PlayerEntity player) {
        try (HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build()) {
            String version = SharedConstants.getGameVersion().getName();
            URI zipUri = URI.create("https://github.com/InventivetalentDev/minecraft-assets/archive/refs/heads/%s.zip".formatted(version));
            Path tempZip = Files.createTempFile("minecraft-assets", ".zip");
            HttpRequest request = buildDownloadRequest(zipUri);
            HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(tempZip));
            validateResponseStatus(response);
            return tempZip;
        } catch (Throwable t) {
            handleDownloadFailure(player, t);
            return null;
        }
    }

    private static HttpRequest buildDownloadRequest(URI zipUri) {
        return HttpRequest.newBuilder(zipUri)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .build();
    }

    private static void validateResponseStatus(@NotNull HttpResponse<Path> response) throws IOException {
        if (response.statusCode() != 200) {
            throw new IOException("Failed to download ZIP: HTTP " + response.statusCode());
        }
    }

    private static void handleDownloadFailure(@NotNull PlayerEntity player, Throwable t) {
        player.sendMessage(Text.translatable("msg." + Pways.MOD_ID + ".vanilla_download_fail").formatted(Formatting.RED, Formatting.ITALIC));
        Pways.LOGGER.error("Failed to download vanilla assets", t);
    }
}