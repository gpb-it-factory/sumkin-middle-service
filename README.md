# sumkin-middle-service
Middle-service repo - the second part of the educational project, featuring major Tg API to emulate a bank-like service.

The service is intended to accept and validate Tg requests and rerout'em to further processing within the backend layer. 

See the diagram for details:

```mermaid
flowchart LR

    Frontend("Frontend
    Telegram-bot
    Выступает как клиентское
    приложение, инициирует
    запросы пользователей")
    
    Middle("Middle
    Java-сервис
    Принимает запросы от телеграм-бота,
    выполняет валидацию и бизнес-логику, 
    маршрутизирует запросы в банк")
    
    Backend("Backend
    Глубинная система
    Выступает в качестве автоматизированной
    банковской системы, обрабатывает 
    транзакции, хранит клиентские данные и т.д.")
    
    Frontend -- http --> Middle -- http -->  Backend 
    
```
