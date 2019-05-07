/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MaTestModule } from '../../../test.module';
import { DataRecordUpdateComponent } from 'app/entities/data-record/data-record-update.component';
import { DataRecordService } from 'app/entities/data-record/data-record.service';
import { DataRecord } from 'app/shared/model/data-record.model';

describe('Component Tests', () => {
    describe('DataRecord Management Update Component', () => {
        let comp: DataRecordUpdateComponent;
        let fixture: ComponentFixture<DataRecordUpdateComponent>;
        let service: DataRecordService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [DataRecordUpdateComponent]
            })
                .overrideTemplate(DataRecordUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DataRecordUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DataRecordService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new DataRecord(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.dataRecord = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new DataRecord();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.dataRecord = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
