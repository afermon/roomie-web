/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { RoomExpenseService } from 'app/entities/room-expense/room-expense.service';
import { IRoomExpense, RoomExpense, CurrencyType } from 'app/shared/model/room-expense.model';

describe('Service Tests', () => {
    describe('RoomExpense Service', () => {
        let injector: TestBed;
        let service: RoomExpenseService;
        let httpMock: HttpTestingController;
        let elemDefault: IRoomExpense;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(RoomExpenseService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new RoomExpense(0, 'AAAAAAA', 'AAAAAAA', CurrencyType.COLON, 0, 0, 0, currentDate, currentDate);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        startDate: currentDate.format(DATE_FORMAT),
                        finishDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a RoomExpense', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        startDate: currentDate.format(DATE_FORMAT),
                        finishDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        startDate: currentDate,
                        finishDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new RoomExpense(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a RoomExpense', async () => {
                const returnedFromService = Object.assign(
                    {
                        name: 'BBBBBB',
                        description: 'BBBBBB',
                        currency: 'BBBBBB',
                        amount: 1,
                        periodicity: 1,
                        monthDay: 1,
                        startDate: currentDate.format(DATE_FORMAT),
                        finishDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        startDate: currentDate,
                        finishDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of RoomExpense', async () => {
                const returnedFromService = Object.assign(
                    {
                        name: 'BBBBBB',
                        description: 'BBBBBB',
                        currency: 'BBBBBB',
                        amount: 1,
                        periodicity: 1,
                        monthDay: 1,
                        startDate: currentDate.format(DATE_FORMAT),
                        finishDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        startDate: currentDate,
                        finishDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a RoomExpense', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
