class Timeline {

    constructor(data = []) {
        this.store = data;
        this.timeline = document.querySelector('#timeline');

        document.getElementById('timeline').addEventListener('click', () => this.writeTweet())

        this.updateTimeline();
    }

    writeTweet() {
        http.createData({
            'text':
            'Lorem ipsum dolor sit amet, consectetur adipiscing elit,' +
            'sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.' +
            ' Lectus mauris ultrices eros in cursus turpis massa. In fermentum et sollicitudin ac orci.' +
            'Faucibus ornare suspendisse sed nisi lacus sed. Vivamus at augue eget arcu dictum varius.' +
            'Lobortis scelerisque fermentum dui faucibus in ornare quam.'
        });
    }

    updateTimeline () {
        this.timeline.innerHTML = '';
        const rows = this.store.map(row => {
            return new ListItem(row).getHtmlElement()
        });
        this.timeline.append(...rows);
    }
}

class ListItem {

    listElm = null;

    constructor(data = []) {
        this.data = data;
        this.init();
    }

    init () {
        this.listElm = document.createElement('li');
        this.listElm.setAttribute('class', 'event');
        let formattedDate = Utils.formatDate(this.data.createdAt);
        this.listElm.setAttribute('data-date', formattedDate);

        const author = document.createElement('h3');
        author.append(this.data.author)
        this.listElm.append(author);

        const text = document.createElement('p');
        text.append(this.data.text)
        this.listElm.append(text);
    }

    getHtmlElement = () => this.listElm;
}

class Utils {
    static padTo2Digits (num) {
        return num.toString().padStart(2, '0');
    }

    static formatDate (date) {
        let d = new Date(date);
        return [
            d.getFullYear(),
            this.padTo2Digits(d.getMonth() + 1),
            this.padTo2Digits(d.getDate()),
        ].join('-');
    }
}