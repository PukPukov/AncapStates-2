# AncapStates
Плагин для MineCraft, позволяющий создавать государства и вести войны.
### Возможности

- Создание городов и государств в игре;
- Контроль территорий по гексагонам;
- Войны между государствами за гексагоны;
- Интеграция с DynMap;
- Система привата для полной защиты территорий (14 обрабатываемых ивентов);
- Система приватных чанков для защиты ресурсов от сограждан;
- Иерархия внутри государства;
- Система чистки неактивных городов;
- Продуманная система сражений и система осады замков (каждая битва может идти по несколько часов!)

### Преимущества
- Отличная оптимизация (0.01% тика/игрок)
- Чистый и расширяемый ООП-код (качество кода 98.1% согласно codefactor.io)
- Проект полностью OpenSource (лицензия MIT)
- API городов и наций для разработчиков (~100 методов)
- AddonAPI для разработчиков
- Собственное сердцебиение плагина для выполнения действий с определённой периодичностью (API имеется)
- Собственное EventAPI (в разработке, предполагается более 50 cancellable-ивентов)
- Сборка плагина с помощью Maven

# AncapStates

![](/META-INF/pic1.png)

![](https://img.shields.io/github/tag/pukpukov/AncapStates?style=for-the-badge&logo=appveyor)

![](https://img.shields.io/github/issues/pukpukov/AncapStates?style=for-the-badge&logo=appveyor) ![](https://img.shields.io/tokei/lines/github/pukpukov/AncapStates?style=for-the-badge&logo=appveyor)

## Города и нации

![](https://img.shields.io/bstats/servers/13812?style=for-the-badge&logo=appveyor) ![](https://img.shields.io/bstats/players/13812?style=for-the-badge&logo=appveyor)

Плагин добавляет в игру обширную систему создания городов и государств (наций). Игроки могут cоздавать города, присоединять новые территории к городу, приглашать других игроков в города и многое другое.

#### Защита территорий городов от посягательств извне
Плагин имеет продвинутую систему привата территорий городов, защищающую от любых взаимодействий с территориями города

#### Приват по гексагонам
Единица территории в AncapStates - гексагон с шириной в 100 блоков.

#### Очистка неактивных городов
Плагин имеет систему удаления неактивных городов - ежедневно ночью с баланса городов (и наций) списывается некоторая сумма ресурсов. Если у города не хватает денег, он удаляется.

## Оптимизация
Плагин имеет отличную оптимизацию и предназначен для работы на серверах с высоким онлайном (200 онлайна предположительно нагрузят сервер не более чем на 2%)
#### Многопоточность
Высокая производительность плагина основывается, в основном, на многопоточном выполнении тяжёлых методов. 

## AncapStates API

![](https://img.shields.io/codefactor/grade/github/PukPukov/AncapStates?style=for-the-badge&logo=appveyor) ![](https://img.shields.io/codeclimate/maintainability-percentage/PukPukov/AncapStates?style=for-the-badge&logo=appveyor)

#### Base API
Базовое API представлено примерно 200 методами у объектов City, Nation и AncapStates.
```java
`Player p = e.getPlayer();
CityMap cityMap = AncapStates.getCityMap();
City city = cityMap.getCity(p.getLocation());
city.sendMessage("В вашем городе гуляет игрок "+p.getName())`
```
#### EventAPI
Невероятно удобное API для разработчиков аддонов, дающее полный контроль над событиями в плагине.
На данный момент находится в разработке.
Планируется добавление около 50 ивентов для городов и наций.
Примерный вид использования:
```java
@EventHandler (priority = EventPriority.LOW)
public void onCityCreation(AncapStatesCityCreateEvent e) {
    AncapPlayer creator = e.getPlayer();
    if (creator.getName().equals("Icarus")) {
		creator.getPlayer().kickPlayer("Ты мне не нравишься");
		e.setCancelled(true);
	}
}
```

## Лицензия

![](https://img.shields.io/github/license/PukPukov/AncapStates?style=for-the-badge&logo=appveyor)

AncapStates лицензирован под MIT Licence. Это означает, что кто угодно может скачать исходный код плагина и делать с ним всё, что он захочет.

## Сборка с помощью Maven

Скачайте zip-архив main ветки и запустите
`mvn package`
в распакованной директории.

.jar файл будет в /target.
