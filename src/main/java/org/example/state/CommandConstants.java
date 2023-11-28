package org.example.state;

public enum CommandConstants {

    CHOSE_HAIRCUT( "Клиент выбрал стрижку %s" ),
    FOR( "Для %s:" ),
    CURRENT_LONG( "\t● Текущая длинна %s милимметров(а)" ),
    DESIRED_LONG( "\t● Желаемая длинна %s миллиметров(а)" ),
    HAIR_COLOR( "\t● Цвет волос %s" ),
    HAIR_STYLING( "\t● Укладка включает %s" ),
    SATISFIED( "Опрос все ли устраивает" ),
    HAIRCUT( "Стрижка %s:" ),
    WASHING_HAIR( "\t● Моем волосы" ),
    HAIRCUT_SECTOR( "\t● Стрижем %s" ),
    COLOR_PROCESS( "\t● Красим волосы" ),
    STYLING_PROCESS( "\t● Укладываем волосы" ),
    DRYING_HAIR( "\t● Сушим волосы" );

    public String stingCommand;

    CommandConstants( String stingCommand ) {
        this.stingCommand = stingCommand;
    }
}