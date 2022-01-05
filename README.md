# AndroidCourse
1. Создан пустой проект
2. Добавлены фрагменты списка и деталей контакта. Реализована нафигация между фрагментами
3. Добавлен сервис для асинхронного получения контактов.
4. Добавлена функция напоминания о дне рождения контакта. Создан BirthdayReceiver, который получает оповещения от AlarmManager.
	Канал нотификации создается перед показом уведомления. Модель контакта теперь содержит поле даты рождения.
	В ContactDetailsFragment добавлена информация о дне рождения контакта, а так же кнопка позволяющая подписаться или отписаться от уведомлений.
5. Загрузка контактов производится из поставщика контактов.
   Добавлены 2 диалога для пользователя первый предупреждает о необходимости предоставления разрешений,
   второй показывается, если пользователь не дал доступ контактов настаивает на этом действии или предлагает выйти из приложения.
6. Произведен рефакторинг проекта с использованием паттерна MVVM.
	Удален сервис получения контактов. Вся логика получения контактов вынесена в ContactRepository.
	Созданы ВьюМодели для экрана списка контактов и  экрана деталей контакта, которые используют ContactRepository для обновления данных