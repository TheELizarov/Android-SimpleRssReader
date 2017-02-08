package ua.motofun.rss.controller;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ua.motofun.rss.http.HttpUtils;
import ua.motofun.rss.http.Request;
import ua.motofun.rss.manager.SettingsManager;
import ua.motofun.rss.model.Chanel;
import ua.motofun.rss.model.Rss;

/**
 * Контроллер для работы с сервисами новостей
 * Created by Elizarov Sergey (elizarov1988@gmail.ru)
 * on 23.03.2015
 */
public abstract class ServiceApiController {
    /**
     * Обновление списка новостей по выбранному в настройках каналу rss
     *
     * @param context
     * @param rssListener callback
     */
    public static void loadRss(Context context, final RssListener rssListener) {
        final Chanel chanel = SettingsManager.getSelectedChanel(context);
        Request.Builder builder = new Request.Builder();
        final Request.ResponseListener responseListener = new Request.ResponseListener() {
            @Override
            public void onSuccess(HttpUtils.HttpResult httpResult) {
                List<Rss> result = new ArrayList<Rss>();
                if (httpResult != null) {
                    try {
                        /**
                         * Не самый лучший парсер xml =)
                         */
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        InputStream stream
                                = new ByteArrayInputStream(httpResult.getEnvelope().getBytes("UTF-8"));
                        Document dom = builder.parse(stream);

                        Element root = dom.getDocumentElement();
                        NodeList items = root.getElementsByTagName("item");

                        for (int i = 0; i < items.getLength(); ++i) {
                            Node item = items.item(i);
                            Rss rss = new Rss(item, chanel.getId());
                            result.add(rss);
                        }
                    } catch (Exception ignored) {
                    }
                }
                if (rssListener != null) {
                    rssListener.onLoaded(result);
                }
            }

            @Override
            public void onError(HttpUtils.HttpResult httpResult) {
                if (rssListener != null) {
                    rssListener.onError(httpResult);
                }
            }
        };
        builder.setUrl(chanel.getLink())
                .setContext(context)
                .setResponseListener(responseListener)
                .build();
    }

    /**
     * Интерфейс callback-а для метода получения списка новостей
     */
    public interface RssListener {
        void onLoaded(List<Rss> loadedRss);
        void onError(HttpUtils.HttpResult result);
    }
}
